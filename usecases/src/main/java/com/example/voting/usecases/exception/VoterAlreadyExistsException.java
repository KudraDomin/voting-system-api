package com.example.voting.usecases.exception;

public class VoterAlreadyExistsException extends RuntimeException {

    public VoterAlreadyExistsException(String name) {
        super("Voter with name '%s' already exists".formatted(name));
    }
}
