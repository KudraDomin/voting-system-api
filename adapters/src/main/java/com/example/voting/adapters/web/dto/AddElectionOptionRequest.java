package com.example.voting.adapters.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload to add an option to an election")
public record AddElectionOptionRequest(

        @Schema(description = "Option label", example = "Candidate A")
        @NotBlank
        @Size(max = ValidationLimits.OPTION_LABEL_MAX_LENGTH)
        String label) {
}
