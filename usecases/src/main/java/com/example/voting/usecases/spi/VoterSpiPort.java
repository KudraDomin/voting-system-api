package com.example.voting.usecases.spi;

import java.util.Optional;

import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;

public interface VoterSpiPort {

    Voter save(Voter voter);

    Optional<Voter> findById(VoterId voterId);

    boolean existsByName(String name);
}
