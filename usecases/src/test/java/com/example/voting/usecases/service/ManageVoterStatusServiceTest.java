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

import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.core.model.VoterStatus;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.VoterSpiPort;

@ExtendWith(MockitoExtension.class)
class ManageVoterStatusServiceTest {

    private static final String VOTER_NAME = "Jane Doe";

    @Mock
    private VoterSpiPort voterSpiPort;

    private ManageVoterStatusService manageVoterStatusService;

    @BeforeEach
    void setUp() {
        manageVoterStatusService = new ManageVoterStatusService(voterSpiPort);
    }

    @Test
    void given_active_voter_when_block_then_returns_blocked_view() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = manageVoterStatusService.blockVoter(voterId);

        // then
        assertThat(view.status()).isEqualTo(VoterStatus.BLOCKED.name());
    }

    @Test
    void given_active_voter_when_block_then_saves_blocked_voter() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));
        var savedVoter = ArgumentCaptor.forClass(Voter.class);

        // when
        manageVoterStatusService.blockVoter(voterId);

        // then
        then(voterSpiPort).should().save(savedVoter.capture());
        assertThat(savedVoter.getValue().isBlocked()).isTrue();
    }

    @Test
    void given_missing_voter_when_block_then_throws_voter_not_found() {
        // given
        var voterId = UUID.randomUUID();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.empty());

        // when
        var thrown = assertThatThrownBy(() -> manageVoterStatusService.blockVoter(voterId));

        // then
        thrown.isInstanceOf(VoterNotFoundException.class);
    }

    @Test
    void given_missing_voter_when_block_then_does_not_save() {
        // given
        var voterId = UUID.randomUUID();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> manageVoterStatusService.blockVoter(voterId));

        // then
        then(voterSpiPort).should(never()).save(any(Voter.class));
    }

    @Test
    void given_blocked_voter_when_unblock_then_returns_active_view() {
        // given
        var voter = Voter.register(VOTER_NAME).block();
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = manageVoterStatusService.unblockVoter(voterId);

        // then
        assertThat(view.status()).isEqualTo(VoterStatus.ACTIVE.name());
    }

    @Test
    void given_blocked_voter_when_unblock_then_saves_active_voter() {
        // given
        var voter = Voter.register(VOTER_NAME).block();
        var voterId = voter.id().value();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.of(voter));
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));
        var savedVoter = ArgumentCaptor.forClass(Voter.class);

        // when
        manageVoterStatusService.unblockVoter(voterId);

        // then
        then(voterSpiPort).should().save(savedVoter.capture());
        assertThat(savedVoter.getValue().isActive()).isTrue();
    }

    @Test
    void given_missing_voter_when_unblock_then_throws_voter_not_found() {
        // given
        var voterId = UUID.randomUUID();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.empty());

        // when
        var thrown = assertThatThrownBy(() -> manageVoterStatusService.unblockVoter(voterId));

        // then
        thrown.isInstanceOf(VoterNotFoundException.class);
    }
}
