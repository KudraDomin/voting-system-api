package com.example.voting.core.model;

import java.util.Objects;

public record Vote(VoteId id, VoterId voterId, ElectionId electionId, ElectionOptionId optionId) {

    public Vote {
        Objects.requireNonNull(id, "vote id must not be null");
        Objects.requireNonNull(voterId, "voter id must not be null");
        Objects.requireNonNull(electionId, "election id must not be null");
        Objects.requireNonNull(optionId, "option id must not be null");
    }

    public static Vote cast(VoterId voterId, ElectionId electionId, ElectionOptionId optionId) {
        return new Vote(VoteId.newId(), voterId, electionId, optionId);
    }
}
