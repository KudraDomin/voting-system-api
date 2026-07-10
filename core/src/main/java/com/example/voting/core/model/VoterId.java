package com.example.voting.core.model;

import java.util.Objects;
import java.util.UUID;

public record VoterId(UUID value) {

    public VoterId {
        Objects.requireNonNull(value, "voter id must not be null");
    }

    public static VoterId newId() {
        return new VoterId(UUID.randomUUID());
    }

    public static VoterId of(UUID value) {
        return new VoterId(value);
    }
}
