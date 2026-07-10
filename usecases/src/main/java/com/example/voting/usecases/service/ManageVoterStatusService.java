package com.example.voting.usecases.service;

import java.util.UUID;

import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.api.BlockVoterApiPort;
import com.example.voting.usecases.api.UnblockVoterApiPort;
import com.example.voting.usecases.api.VoterView;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.VoterSpiPort;

public class ManageVoterStatusService implements BlockVoterApiPort, UnblockVoterApiPort {

    private final VoterSpiPort voterSpiPort;

    public ManageVoterStatusService(VoterSpiPort voterSpiPort) {
        this.voterSpiPort = voterSpiPort;
    }

    @Override
    public VoterView blockVoter(UUID voterId) {
        var blockedVoter = findVoter(voterId).block();

        return VoterView.from(voterSpiPort.save(blockedVoter));
    }

    @Override
    public VoterView unblockVoter(UUID voterId) {
        var unblockedVoter = findVoter(voterId).unblock();

        return VoterView.from(voterSpiPort.save(unblockedVoter));
    }

    private Voter findVoter(UUID voterId) {
        return voterSpiPort.findById(VoterId.of(voterId))
                .orElseThrow(() -> new VoterNotFoundException(voterId));
    }
}
