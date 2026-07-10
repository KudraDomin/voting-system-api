package com.example.voting.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.voting.core.service.VoteFactory;
import com.example.voting.usecases.api.AddElectionOptionApiPort;
import com.example.voting.usecases.api.CastVoteApiPort;
import com.example.voting.usecases.api.CreateElectionApiPort;
import com.example.voting.usecases.api.GetElectionApiPort;
import com.example.voting.usecases.api.GetVoterApiPort;
import com.example.voting.usecases.api.RegisterVoterApiPort;
import com.example.voting.usecases.service.AddElectionOptionService;
import com.example.voting.usecases.service.CastVoteService;
import com.example.voting.usecases.service.CreateElectionService;
import com.example.voting.usecases.service.ManageVoterStatusService;
import com.example.voting.usecases.service.QueryElectionService;
import com.example.voting.usecases.service.QueryVoterService;
import com.example.voting.usecases.service.RegisterVoterService;
import com.example.voting.usecases.spi.ElectionSpiPort;
import com.example.voting.usecases.spi.VoteSpiPort;
import com.example.voting.usecases.spi.VoterSpiPort;

@Configuration
public class UseCaseConfig {

    @Bean
    public VoteFactory voteFactory() {
        return new VoteFactory();
    }

    @Bean
    public RegisterVoterApiPort registerVoterApiPort(VoterSpiPort voterSpiPort) {
        return new RegisterVoterService(voterSpiPort);
    }

    @Bean
    public ManageVoterStatusService manageVoterStatusService(VoterSpiPort voterSpiPort) {
        return new ManageVoterStatusService(voterSpiPort);
    }

    @Bean
    public CreateElectionApiPort createElectionApiPort(ElectionSpiPort electionSpiPort) {
        return new CreateElectionService(electionSpiPort);
    }

    @Bean
    public AddElectionOptionApiPort addElectionOptionApiPort(ElectionSpiPort electionSpiPort) {
        return new AddElectionOptionService(electionSpiPort);
    }

    @Bean
    public CastVoteApiPort castVoteApiPort(
            VoterSpiPort voterSpiPort,
            ElectionSpiPort electionSpiPort,
            VoteSpiPort voteSpiPort,
            VoteFactory voteFactory) {
        return new CastVoteService(voterSpiPort, electionSpiPort, voteSpiPort, voteFactory);
    }

    @Bean
    public GetElectionApiPort getElectionApiPort(ElectionSpiPort electionSpiPort) {
        return new QueryElectionService(electionSpiPort);
    }

    @Bean
    public GetVoterApiPort getVoterApiPort(VoterSpiPort voterSpiPort) {
        return new QueryVoterService(voterSpiPort);
    }
}
