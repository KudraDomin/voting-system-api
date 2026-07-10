package com.example.voting.core.exception;

import com.example.voting.core.model.VoterId;

public class BlockedVoterCannotVoteException extends RuntimeException {

    public BlockedVoterCannotVoteException(VoterId voterId) {
        super("Voter %s is blocked and cannot vote".formatted(voterId.value()));
    }
}
