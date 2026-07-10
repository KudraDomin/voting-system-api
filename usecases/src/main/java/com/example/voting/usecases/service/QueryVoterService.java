package com.example.voting.usecases.service;

import java.util.UUID;

import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.api.GetVoterApiPort;
import com.example.voting.usecases.api.VoterView;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.VoterSpiPort;

public class QueryVoterService implements GetVoterApiPort {

    private final VoterSpiPort voterSpiPort;

    public QueryVoterService(VoterSpiPort voterSpiPort) {
        this.voterSpiPort = voterSpiPort;
    }

    @Override
    public VoterView getVoter(UUID voterId) {
        return voterSpiPort.findById(VoterId.of(voterId))
                .map(VoterView::from)
                .orElseThrow(() -> new VoterNotFoundException(voterId));
    }
}
