package com.example.voting.core.service;

import com.example.voting.core.exception.BlockedVoterCannotVoteException;
import com.example.voting.core.exception.OptionNotInElectionException;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionOptionId;
import com.example.voting.core.model.Vote;
import com.example.voting.core.model.Voter;

public final class VoteFactory {

    public Vote castVote(Voter voter, Election election, ElectionOptionId optionId) {
        requireActiveVoter(voter);
        requireOptionBelongsToElection(election, optionId);

        return Vote.cast(voter.id(), election.id(), optionId);
    }

    private void requireActiveVoter(Voter voter) {
        if (voter.isBlocked()) {
            throw new BlockedVoterCannotVoteException(voter.id());
        }
    }

    private void requireOptionBelongsToElection(Election election, ElectionOptionId optionId) {
        if (!election.hasOption(optionId)) {
            throw new OptionNotInElectionException(election.id(), optionId);
        }
    }
}
