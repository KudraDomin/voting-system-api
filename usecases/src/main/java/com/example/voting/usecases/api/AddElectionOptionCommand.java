package com.example.voting.usecases.api;

import java.util.UUID;

public record AddElectionOptionCommand(UUID electionId, String label) {
}
