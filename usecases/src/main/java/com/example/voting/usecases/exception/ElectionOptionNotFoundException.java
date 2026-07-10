package com.example.voting.usecases.exception;

import java.util.UUID;

public class ElectionOptionNotFoundException extends RuntimeException {

    public ElectionOptionNotFoundException(UUID optionId) {
        super("Election option %s was not found".formatted(optionId));
    }
}
