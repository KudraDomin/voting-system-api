package com.example.voting.adapters.web.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Voter representation returned by the API")
public record VoterResponse(UUID id, String name, String status) {
}
