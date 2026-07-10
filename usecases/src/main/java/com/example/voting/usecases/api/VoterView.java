package com.example.voting.usecases.api;

import java.util.UUID;

import com.example.voting.core.model.Voter;

public record VoterView(UUID id, String name, String status) {

    public static VoterView from(Voter voter) {
        return new VoterView(voter.id().value(), voter.name(), voter.status().name());
    }
}
