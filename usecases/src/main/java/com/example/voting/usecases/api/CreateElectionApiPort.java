package com.example.voting.usecases.api;

public interface CreateElectionApiPort {

    ElectionView createElection(CreateElectionCommand command);
}
