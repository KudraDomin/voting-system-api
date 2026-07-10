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

import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;

@ExtendWith(MockitoExtension.class)
class QueryElectionServiceTest {

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    @Mock
    private ElectionSpiPort electionSpiPort;

    private QueryElectionService queryElectionService;

    @BeforeEach
    void setUp() {
        queryElectionService = new QueryElectionService(electionSpiPort);
    }

    @Test
    void given_existing_election_when_get_election_then_returns_view_with_id() {
        // given
        var election = Election.create(ELECTION_TITLE);
        var electionId = election.id().value();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.of(election));

        // when
        var view = queryElectionService.getElection(electionId);

        // then
        assertThat(view.id()).isEqualTo(electionId);
    }

    @Test
    void given_existing_election_when_get_election_then_returns_view_with_title() {
        // given
        var election = Election.create(ELECTION_TITLE);
        var electionId = election.id().value();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.of(election));

        // when
        var view = queryElectionService.getElection(electionId);

        // then
        assertThat(view.title()).isEqualTo(ELECTION_TITLE);
    }

    @Test
    void given_missing_election_when_get_election_then_throws_election_not_found() {
        // given
        var electionId = UUID.randomUUID();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.empty());

        // when
        var thrown = assertThatThrownBy(() -> queryElectionService.getElection(electionId));

        // then
        thrown.isInstanceOf(ElectionNotFoundException.class);
    }
}
