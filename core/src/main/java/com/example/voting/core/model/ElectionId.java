package com.example.voting.core.model;

import java.util.Objects;
import java.util.UUID;

public record ElectionId(UUID value) {

    public ElectionId {
        Objects.requireNonNull(value, "election id must not be null");
    }

    public static ElectionId newId() {
        return new ElectionId(UUID.randomUUID());
    }

    public static ElectionId of(UUID value) {
        return new ElectionId(value);
    }
}
