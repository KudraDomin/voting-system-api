package com.example.voting.usecases.api;

import java.util.UUID;

public interface GetVoterApiPort {

    VoterView getVoter(UUID voterId);
}
