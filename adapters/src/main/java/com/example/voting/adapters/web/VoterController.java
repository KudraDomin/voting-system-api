package com.example.voting.adapters.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.voting.adapters.metrics.VotingMetrics;
import com.example.voting.adapters.web.dto.RegisterVoterRequest;
import com.example.voting.adapters.web.dto.VoterResponse;
import com.example.voting.adapters.web.mapper.VoterWebMapper;
import com.example.voting.usecases.api.BlockVoterApiPort;
import com.example.voting.usecases.api.GetVoterApiPort;
import com.example.voting.usecases.api.RegisterVoterApiPort;
import com.example.voting.usecases.api.UnblockVoterApiPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/voters")
@RequiredArgsConstructor
@Tag(name = "Voters", description = "Voter registration and status management")
public class VoterController {

    private final RegisterVoterApiPort registerVoterApiPort;

    private final BlockVoterApiPort blockVoterApiPort;

    private final UnblockVoterApiPort unblockVoterApiPort;

    private final GetVoterApiPort getVoterApiPort;

    private final VoterWebMapper voterWebMapper;

    private final VotingMetrics votingMetrics;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new voter")
    @ApiResponse(responseCode = "201", description = "Voter registered")
    public VoterResponse registerVoter(@Valid @RequestBody RegisterVoterRequest request) {
        var voterView = registerVoterApiPort.registerVoter(voterWebMapper.toCommand(request));
        votingMetrics.recordVoterRegistered();

        return voterWebMapper.toResponse(voterView);
    }

    @GetMapping("/{voterId}")
    @Operation(summary = "Get a voter by identifier")
    @ApiResponse(responseCode = "200", description = "Voter found")
    public VoterResponse getVoter(@PathVariable UUID voterId) {
        return voterWebMapper.toResponse(getVoterApiPort.getVoter(voterId));
    }

    @PostMapping("/{voterId}/block")
    @Operation(summary = "Block a voter")
    @ApiResponse(responseCode = "200", description = "Voter blocked")
    public VoterResponse blockVoter(@PathVariable UUID voterId) {
        return voterWebMapper.toResponse(blockVoterApiPort.blockVoter(voterId));
    }

    @PostMapping("/{voterId}/unblock")
    @Operation(summary = "Unblock a voter")
    @ApiResponse(responseCode = "200", description = "Voter unblocked")
    public VoterResponse unblockVoter(@PathVariable UUID voterId) {
        return voterWebMapper.toResponse(unblockVoterApiPort.unblockVoter(voterId));
    }
}
