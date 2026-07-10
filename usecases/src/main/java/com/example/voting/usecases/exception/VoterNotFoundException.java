package com.example.voting.usecases.exception;

import java.util.UUID;

public class VoterNotFoundException extends RuntimeException {

    public VoterNotFoundException(UUID voterId) {
        super("Voter %s was not found".formatted(voterId));
    }
}
