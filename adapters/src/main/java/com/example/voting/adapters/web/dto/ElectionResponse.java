package com.example.voting.adapters.web.dto;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Election representation including its options")
public record ElectionResponse(UUID id, String title, List<ElectionOptionResponse> options) {
}
