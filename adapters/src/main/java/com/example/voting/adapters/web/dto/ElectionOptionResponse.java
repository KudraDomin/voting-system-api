package com.example.voting.adapters.web.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Election option representation returned by the API")
public record ElectionOptionResponse(UUID id, String label) {
}
