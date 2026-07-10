package com.example.voting.adapters.web.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Vote representation returned by the API")
public record VoteResponse(UUID id, UUID electionId, UUID voterId, UUID optionId) {
}
