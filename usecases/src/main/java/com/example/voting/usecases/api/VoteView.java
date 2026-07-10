package com.example.voting.usecases.api;

import java.util.UUID;

import com.example.voting.core.model.Vote;

public record VoteView(UUID id, UUID electionId, UUID voterId, UUID optionId) {

    public static VoteView from(Vote vote) {
        return new VoteView(
                vote.id().value(),
                vote.electionId().value(),
                vote.voterId().value(),
                vote.optionId().value());
    }
}
