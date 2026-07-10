package com.example.voting.usecases.api;

import java.util.UUID;

public record CastVoteCommand(UUID electionId, UUID voterId, UUID optionId) {
}
