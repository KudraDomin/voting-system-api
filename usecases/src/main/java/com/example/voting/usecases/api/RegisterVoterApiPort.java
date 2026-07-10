package com.example.voting.usecases.api;

public interface RegisterVoterApiPort {

    VoterView registerVoter(RegisterVoterCommand command);
}
