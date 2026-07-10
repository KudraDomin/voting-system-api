package com.example.voting.core.model;

import java.util.Objects;

public record ElectionOption(ElectionOptionId id, ElectionId electionId, String label) {

    private static final int MAX_LABEL_LENGTH = 200;

    public ElectionOption {
        Objects.requireNonNull(id, "option id must not be null");
        Objects.requireNonNull(electionId, "election id must not be null");
        label = requireValidLabel(label);
    }

    public static ElectionOption create(ElectionId electionId, String label) {
        return new ElectionOption(ElectionOptionId.newId(), electionId, label);
    }

    private static String requireValidLabel(String label) {
        Objects.requireNonNull(label, "option label must not be null");

        var trimmed = label.strip();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("option label must not be blank");
        }

        if (trimmed.length() > MAX_LABEL_LENGTH) {
            throw new IllegalArgumentException("option label exceeds maximum length");
        }

        return trimmed;
    }
}
