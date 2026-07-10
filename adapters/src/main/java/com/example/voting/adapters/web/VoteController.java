package com.example.voting.adapters.web;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.voting.adapters.config.CacheConfig;
import com.example.voting.adapters.metrics.VotingMetrics;
import com.example.voting.adapters.web.dto.CastVoteRequest;
import com.example.voting.adapters.web.dto.VoteResponse;
import com.example.voting.adapters.web.mapper.VoteWebMapper;
import com.example.voting.usecases.api.CastVoteApiPort;
import com.example.voting.usecases.api.CastVoteCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/elections/{electionId}/votes")
@RequiredArgsConstructor
@Tag(name = "Votes", description = "Vote casting")
public class VoteController {

    private final CastVoteApiPort castVoteApiPort;

    private final VoteWebMapper voteWebMapper;

    private final VotingMetrics votingMetrics;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames = CacheConfig.ELECTIONS_CACHE, key = "#electionId")
    @Operation(summary = "Cast a vote in an election")
    @ApiResponse(responseCode = "201", description = "Vote registered")
    public VoteResponse castVote(
            @PathVariable UUID electionId,
            @Valid @RequestBody CastVoteRequest request) {
        var command = new CastVoteCommand(electionId, request.voterId(), request.optionId());

        var voteView = votingMetrics.recordCastVote(() -> castVoteApiPort.castVote(command));

        return voteWebMapper.toResponse(voteView);
    }
}
