package com.example.voting.usecases.api;

public interface AddElectionOptionApiPort {

    ElectionView addOption(AddElectionOptionCommand command);
}
