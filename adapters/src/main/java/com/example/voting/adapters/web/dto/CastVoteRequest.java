package com.example.voting.adapters.web.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload to cast a vote in an election")
public record CastVoteRequest(

        @Schema(description = "Identifier of the voter casting the vote")
        @NotNull
        UUID voterId,

        @Schema(description = "Identifier of the chosen option")
        @NotNull
        UUID optionId) {
}
