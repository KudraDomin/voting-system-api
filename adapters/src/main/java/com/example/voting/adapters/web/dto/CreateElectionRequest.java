package com.example.voting.adapters.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload to create a new election")
public record CreateElectionRequest(

        @Schema(description = "Election title", example = "Mayor Election 2025")
        @NotBlank
        @Size(max = ValidationLimits.ELECTION_TITLE_MAX_LENGTH)
        String title) {
}
