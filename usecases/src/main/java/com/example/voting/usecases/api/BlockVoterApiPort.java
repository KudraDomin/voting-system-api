package com.example.voting.usecases.api;

import java.util.UUID;

public interface BlockVoterApiPort {

    VoterView blockVoter(UUID voterId);
}
