package com.example.voting.usecases.service;

import java.util.UUID;

import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.usecases.api.AddElectionOptionApiPort;
import com.example.voting.usecases.api.AddElectionOptionCommand;
import com.example.voting.usecases.api.ElectionView;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;

public class AddElectionOptionService implements AddElectionOptionApiPort {

    private final ElectionSpiPort electionSpiPort;

    public AddElectionOptionService(ElectionSpiPort electionSpiPort) {
        this.electionSpiPort = electionSpiPort;
    }

    @Override
    public ElectionView addOption(AddElectionOptionCommand command) {
        var election = findElection(command.electionId());

        var updatedElection = electionSpiPort.save(election.addOption(command.label()));

        return ElectionView.from(updatedElection);
    }

    private Election findElection(UUID electionId) {
        return electionSpiPort.findById(ElectionId.of(electionId))
                .orElseThrow(() -> new ElectionNotFoundException(electionId));
    }
}
