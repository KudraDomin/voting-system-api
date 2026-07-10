package com.example.voting.usecases.service;

import com.example.voting.core.model.Voter;
import com.example.voting.usecases.api.RegisterVoterApiPort;
import com.example.voting.usecases.api.RegisterVoterCommand;
import com.example.voting.usecases.api.VoterView;
import com.example.voting.usecases.exception.VoterAlreadyExistsException;
import com.example.voting.usecases.spi.VoterSpiPort;

public class RegisterVoterService implements RegisterVoterApiPort {

    private final VoterSpiPort voterSpiPort;

    public RegisterVoterService(VoterSpiPort voterSpiPort) {
        this.voterSpiPort = voterSpiPort;
    }

    @Override
    public VoterView registerVoter(RegisterVoterCommand command) {
        requireNameAvailable(command.name());

        var registeredVoter = voterSpiPort.save(Voter.register(command.name()));

        return VoterView.from(registeredVoter);
    }

    private void requireNameAvailable(String name) {
        if (voterSpiPort.existsByName(name)) {
            throw new VoterAlreadyExistsException(name);
        }
    }
}
