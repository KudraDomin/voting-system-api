package com.example.voting.usecases.spi;

import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.Vote;
import com.example.voting.core.model.VoterId;

public interface VoteSpiPort {

    Vote save(Vote vote);

    boolean existsByVoterAndElection(VoterId voterId, ElectionId electionId);
}
