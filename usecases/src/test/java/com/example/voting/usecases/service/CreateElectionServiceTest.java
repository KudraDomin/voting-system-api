package com.example.voting.usecases.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.voting.core.model.Election;
import com.example.voting.usecases.api.CreateElectionCommand;
import com.example.voting.usecases.spi.ElectionSpiPort;

@ExtendWith(MockitoExtension.class)
class CreateElectionServiceTest {

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    @Mock
    private ElectionSpiPort electionSpiPort;

    private CreateElectionService createElectionService;

    @BeforeEach
    void setUp() {
        createElectionService = new CreateElectionService(electionSpiPort);
    }

    @Test
    void given_title_when_create_election_then_returns_view_with_title() {
        // given
        given(electionSpiPort.save(any(Election.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = createElectionService.createElection(new CreateElectionCommand(ELECTION_TITLE));

        // then
        assertThat(view.title()).isEqualTo(ELECTION_TITLE);
    }

    @Test
    void given_title_when_create_election_then_returns_view_without_options() {
        // given
        given(electionSpiPort.save(any(Election.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = createElectionService.createElection(new CreateElectionCommand(ELECTION_TITLE));

        // then
        assertThat(view.options()).isEmpty();
    }

    @Test
    void given_title_when_create_election_then_saves_election_with_title() {
        // given
        given(electionSpiPort.save(any(Election.class))).willAnswer(invocation -> invocation.getArgument(0));
        var savedElection = ArgumentCaptor.forClass(Election.class);

        // when
        createElectionService.createElection(new CreateElectionCommand(ELECTION_TITLE));

        // then
        then(electionSpiPort).should().save(savedElection.capture());
        assertThat(savedElection.getValue().title()).isEqualTo(ELECTION_TITLE);
    }
}
