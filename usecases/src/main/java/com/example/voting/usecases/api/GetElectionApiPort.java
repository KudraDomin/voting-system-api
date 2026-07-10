package com.example.voting.usecases.api;

import java.util.UUID;

public interface GetElectionApiPort {

    ElectionView getElection(UUID electionId);
}
