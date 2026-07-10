package com.example.voting.core.exception;

import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.VoterId;

public class DuplicateVoteException extends RuntimeException {

    public DuplicateVoteException(VoterId voterId, ElectionId electionId) {
        super("Voter %s has already voted in election %s".formatted(voterId.value(), electionId.value()));
    }
}
