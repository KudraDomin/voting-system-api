package com.example.voting.core.exception;

import com.example.voting.core.model.VoterId;

public class VoterNotBlockedException extends RuntimeException {

    public VoterNotBlockedException(VoterId voterId) {
        super("Voter %s is not blocked".formatted(voterId.value()));
    }
}
