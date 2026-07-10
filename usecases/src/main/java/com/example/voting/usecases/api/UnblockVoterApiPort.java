package com.example.voting.usecases.api;

import java.util.UUID;

public interface UnblockVoterApiPort {

    VoterView unblockVoter(UUID voterId);
}
