package com.example.voting.usecases.service;

import java.util.UUID;

import com.example.voting.core.model.ElectionId;
import com.example.voting.usecases.api.ElectionView;
import com.example.voting.usecases.api.GetElectionApiPort;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;

public class QueryElectionService implements GetElectionApiPort {

    private final ElectionSpiPort electionSpiPort;

    public QueryElectionService(ElectionSpiPort electionSpiPort) {
        this.electionSpiPort = electionSpiPort;
    }

    @Override
    public ElectionView getElection(UUID electionId) {
        return electionSpiPort.findById(ElectionId.of(electionId))
                .map(ElectionView::from)
                .orElseThrow(() -> new ElectionNotFoundException(electionId));
    }
}
