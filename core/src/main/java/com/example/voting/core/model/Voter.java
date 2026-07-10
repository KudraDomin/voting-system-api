package com.example.voting.core.model;

import java.util.Objects;

import com.example.voting.core.exception.VoterAlreadyBlockedException;
import com.example.voting.core.exception.VoterNotBlockedException;

public final class Voter {

    private static final int MAX_NAME_LENGTH = 120;

    private final VoterId id;

    private final String name;

    private final VoterStatus status;

    public Voter(VoterId id, String name, VoterStatus status) {
        this.id = Objects.requireNonNull(id, "voter id must not be null");
        this.name = requireValidName(name);
        this.status = Objects.requireNonNull(status, "voter status must not be null");
    }

    public static Voter register(String name) {
        return new Voter(VoterId.newId(), name, VoterStatus.ACTIVE);
    }

    public Voter block() {
        requireNotAlreadyBlocked();

        return new Voter(id, name, VoterStatus.BLOCKED);
    }

    public Voter unblock() {
        requireCurrentlyBlocked();

        return new Voter(id, name, VoterStatus.ACTIVE);
    }

    public boolean isActive() {
        return status == VoterStatus.ACTIVE;
    }

    public boolean isBlocked() {
        return status == VoterStatus.BLOCKED;
    }

    public VoterId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public VoterStatus status() {
        return status;
    }

    private void requireNotAlreadyBlocked() {
        if (isBlocked()) {
            throw new VoterAlreadyBlockedException(id);
        }
    }

    private void requireCurrentlyBlocked() {
        if (isActive()) {
            throw new VoterNotBlockedException(id);
        }
    }

    private static String requireValidName(String name) {
        Objects.requireNonNull(name, "voter name must not be null");

        var trimmed = name.strip();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("voter name must not be blank");
        }

        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("voter name exceeds maximum length");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Voter voter)) {
            return false;
        }

        return id.equals(voter.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
