package com.example.voting.usecases.service;

import com.example.voting.core.model.Election;
import com.example.voting.usecases.api.CreateElectionApiPort;
import com.example.voting.usecases.api.CreateElectionCommand;
import com.example.voting.usecases.api.ElectionView;
import com.example.voting.usecases.spi.ElectionSpiPort;

public class CreateElectionService implements CreateElectionApiPort {

    private final ElectionSpiPort electionSpiPort;

    public CreateElectionService(ElectionSpiPort electionSpiPort) {
        this.electionSpiPort = electionSpiPort;
    }

    @Override
    public ElectionView createElection(CreateElectionCommand command) {
        var createdElection = electionSpiPort.save(Election.create(command.title()));

        return ElectionView.from(createdElection);
    }
}
