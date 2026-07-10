package com.example.voting.usecases.exception;

import java.util.UUID;

public class ElectionNotFoundException extends RuntimeException {

    public ElectionNotFoundException(UUID electionId) {
        super("Election %s was not found".formatted(electionId));
    }
}
