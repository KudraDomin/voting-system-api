package com.example.voting.core.exception;

import com.example.voting.core.model.VoterId;

public class VoterAlreadyBlockedException extends RuntimeException {

    public VoterAlreadyBlockedException(VoterId voterId) {
        super("Voter %s is already blocked".formatted(voterId.value()));
    }
}
