package com.example.voting.core.model;

import java.util.Objects;
import java.util.UUID;

public record VoteId(UUID value) {

    public VoteId {
        Objects.requireNonNull(value, "vote id must not be null");
    }

    public static VoteId newId() {
        return new VoteId(UUID.randomUUID());
    }

    public static VoteId of(UUID value) {
        return new VoteId(value);
    }
}
