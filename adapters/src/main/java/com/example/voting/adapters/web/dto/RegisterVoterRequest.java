package com.example.voting.adapters.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload to register a new voter")
public record RegisterVoterRequest(

        @Schema(description = "Unique voter name", example = "Jane Doe")
        @NotBlank
        @Size(max = ValidationLimits.VOTER_NAME_MAX_LENGTH)
        String name) {
}
