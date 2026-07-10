package com.example.voting.usecases.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.usecases.api.AddElectionOptionCommand;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;

@ExtendWith(MockitoExtension.class)
class AddElectionOptionServiceTest {

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    private static final String OPTION_LABEL = "Candidate A";

    @Mock
    private ElectionSpiPort electionSpiPort;

    private AddElectionOptionService addElectionOptionService;

    @BeforeEach
    void setUp() {
        addElectionOptionService = new AddElectionOptionService(electionSpiPort);
    }

    @Test
    void given_existing_election_when_add_option_then_returns_view_with_option() {
        // given
        var election = Election.create(ELECTION_TITLE);
        var electionId = election.id().value();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.of(election));
        given(electionSpiPort.save(any(Election.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = addElectionOptionService.addOption(new AddElectionOptionCommand(electionId, OPTION_LABEL));

        // then
        assertThat(view.options()).extracting("label").containsExactly(OPTION_LABEL);
    }

    @Test
    void given_existing_election_when_add_option_then_saves_election_with_option() {
        // given
        var election = Election.create(ELECTION_TITLE);
        var electionId = election.id().value();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.of(election));
        given(electionSpiPort.save(any(Election.class))).willAnswer(invocation -> invocation.getArgument(0));
        var savedElection = ArgumentCaptor.forClass(Election.class);

        // when
        addElectionOptionService.addOption(new AddElectionOptionCommand(electionId, OPTION_LABEL));

        // then
        then(electionSpiPort).should().save(savedElection.capture());
        assertThat(savedElection.getValue().options()).hasSize(1);
    }

    @Test
    void given_missing_election_when_add_option_then_throws_election_not_found() {
        // given
        var electionId = UUID.randomUUID();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.empty());

        // when
        var thrown = assertThatThrownBy(
                () -> addElectionOptionService.addOption(new AddElectionOptionCommand(electionId, OPTION_LABEL)));

        // then
        thrown.isInstanceOf(ElectionNotFoundException.class);
    }

    @Test
    void given_missing_election_when_add_option_then_does_not_save() {
        // given
        var electionId = UUID.randomUUID();
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(
                () -> addElectionOptionService.addOption(new AddElectionOptionCommand(electionId, OPTION_LABEL)));

        // then
        then(electionSpiPort).should(never()).save(any(Election.class));
    }
}
