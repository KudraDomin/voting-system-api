package com.example.voting.core.model;

import java.util.Objects;
import java.util.UUID;

public record ElectionOptionId(UUID value) {

    public ElectionOptionId {
        Objects.requireNonNull(value, "election option id must not be null");
    }

    public static ElectionOptionId newId() {
        return new ElectionOptionId(UUID.randomUUID());
    }

    public static ElectionOptionId of(UUID value) {
        return new ElectionOptionId(value);
    }
}
