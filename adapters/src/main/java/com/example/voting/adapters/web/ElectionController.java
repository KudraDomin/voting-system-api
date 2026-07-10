package com.example.voting.adapters.web;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.voting.adapters.config.CacheConfig;
import com.example.voting.adapters.web.dto.AddElectionOptionRequest;
import com.example.voting.adapters.web.dto.CreateElectionRequest;
import com.example.voting.adapters.web.dto.ElectionResponse;
import com.example.voting.adapters.web.mapper.ElectionWebMapper;
import com.example.voting.usecases.api.AddElectionOptionApiPort;
import com.example.voting.usecases.api.AddElectionOptionCommand;
import com.example.voting.usecases.api.CreateElectionApiPort;
import com.example.voting.usecases.api.GetElectionApiPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
@Tag(name = "Elections", description = "Election and option management")
public class ElectionController {

    private final CreateElectionApiPort createElectionApiPort;

    private final AddElectionOptionApiPort addElectionOptionApiPort;

    private final GetElectionApiPort getElectionApiPort;

    private final ElectionWebMapper electionWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new election")
    @ApiResponse(responseCode = "201", description = "Election created")
    public ElectionResponse createElection(@Valid @RequestBody CreateElectionRequest request) {
        var electionView = createElectionApiPort.createElection(electionWebMapper.toCommand(request));

        return electionWebMapper.toResponse(electionView);
    }

    @GetMapping("/{electionId}")
    @Cacheable(cacheNames = CacheConfig.ELECTIONS_CACHE, key = "#electionId")
    @Operation(summary = "Get an election with its options")
    @ApiResponse(responseCode = "200", description = "Election found")
    public ElectionResponse getElection(@PathVariable UUID electionId) {
        return electionWebMapper.toResponse(getElectionApiPort.getElection(electionId));
    }

    @PostMapping("/{electionId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames = CacheConfig.ELECTIONS_CACHE, key = "#electionId")
    @Operation(summary = "Add an option to an election")
    @ApiResponse(responseCode = "201", description = "Option added")
    public ElectionResponse addOption(
            @PathVariable UUID electionId,
            @Valid @RequestBody AddElectionOptionRequest request) {
        var command = new AddElectionOptionCommand(electionId, request.label());

        return electionWebMapper.toResponse(addElectionOptionApiPort.addOption(command));
    }
}
