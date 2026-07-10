package com.example.voting.usecases.spi;

import java.util.Optional;

import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;

public interface ElectionSpiPort {

    Election save(Election election);

    Optional<Election> findById(ElectionId electionId);
}
