package com.example.voting.usecases.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.VoterSpiPort;

@ExtendWith(MockitoExtension.class)
class QueryVoterServiceTest {

    private static final String VOTER_NAME = "Jane Doe";

    @Mock
    private VoterSpiPort voterSpiPort;

    private QueryVoterService queryVoterService;

    @BeforeEach
    void setUp() {
        queryVoterService = new QueryVoterService(voterSpiPort);
    }

    @Test
    void given_existing_voter_when_get_voter_then_returns_view_with_id() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));

        // when
        var view = queryVoterService.getVoter(voterId);

        // then
        assertThat(view.id()).isEqualTo(voterId);
    }

    @Test
    void given_existing_voter_when_get_voter_then_returns_view_with_name() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));

        // when
        var view = queryVoterService.getVoter(voterId);

        // then
        assertThat(view.name()).isEqualTo(VOTER_NAME);
    }

    @Test
    void given_missing_voter_when_get_voter_then_throws_voter_not_found() {
        // given
        var voterId = UUID.randomUUID();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.empty());

        // when
        var thrown = assertThatThrownBy(() -> queryVoterService.getVoter(voterId));

        // then
        thrown.isInstanceOf(VoterNotFoundException.class);
    }
}
