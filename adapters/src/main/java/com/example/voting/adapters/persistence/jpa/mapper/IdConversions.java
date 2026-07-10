package com.example.voting.adapters.persistence.jpa.mapper;

import java.util.UUID;

import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.ElectionOptionId;
import com.example.voting.core.model.VoteId;
import com.example.voting.core.model.VoterId;

public interface IdConversions {

    default UUID fromVoterId(VoterId voterId) {
        return voterId == null ? null : voterId.value();
    }

    default VoterId toVoterId(UUID value) {
        return value == null ? null : VoterId.of(value);
    }

    default UUID fromElectionId(ElectionId electionId) {
        return electionId == null ? null : electionId.value();
    }

    default ElectionId toElectionId(UUID value) {
        return value == null ? null : ElectionId.of(value);
    }

    default UUID fromElectionOptionId(ElectionOptionId optionId) {
        return optionId == null ? null : optionId.value();
    }

    default ElectionOptionId toElectionOptionId(UUID value) {
        return value == null ? null : ElectionOptionId.of(value);
    }

    default UUID fromVoteId(VoteId voteId) {
        return voteId == null ? null : voteId.value();
    }

    default VoteId toVoteId(UUID value) {
        return value == null ? null : VoteId.of(value);
    }
}
