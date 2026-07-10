package com.example.voting.usecases.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterStatus;
import com.example.voting.usecases.api.RegisterVoterCommand;
import com.example.voting.usecases.exception.VoterAlreadyExistsException;
import com.example.voting.usecases.spi.VoterSpiPort;

@ExtendWith(MockitoExtension.class)
class RegisterVoterServiceTest {

    private static final String VOTER_NAME = "Jane Doe";

    @Mock
    private VoterSpiPort voterSpiPort;

    private RegisterVoterService registerVoterService;

    @BeforeEach
    void setUp() {
        registerVoterService = new RegisterVoterService(voterSpiPort);
    }

    @Test
    void given_new_name_when_register_voter_then_returns_view_with_name() {
        // given
        given(voterSpiPort.existsByName(VOTER_NAME)).willReturn(false);
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = registerVoterService.registerVoter(new RegisterVoterCommand(VOTER_NAME));

        // then
        assertThat(view.name()).isEqualTo(VOTER_NAME);
    }

    @Test
    void given_new_name_when_register_voter_then_returned_view_is_active() {
        // given
        given(voterSpiPort.existsByName(VOTER_NAME)).willReturn(false);
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var view = registerVoterService.registerVoter(new RegisterVoterCommand(VOTER_NAME));

        // then
        assertThat(view.status()).isEqualTo(VoterStatus.ACTIVE.name());
    }

    @Test
    void given_new_name_when_register_voter_then_saves_active_voter_with_name() {
        // given
        given(voterSpiPort.existsByName(VOTER_NAME)).willReturn(false);
        given(voterSpiPort.save(any(Voter.class))).willAnswer(invocation -> invocation.getArgument(0));
        var savedVoter = ArgumentCaptor.forClass(Voter.class);

        // when
        registerVoterService.registerVoter(new RegisterVoterCommand(VOTER_NAME));

        // then
        then(voterSpiPort).should().save(savedVoter.capture());
        assertThat(savedVoter.getValue().name()).isEqualTo(VOTER_NAME);
        assertThat(savedVoter.getValue().isActive()).isTrue();
    }

    @Test
    void given_existing_name_when_register_voter_then_throws_voter_already_exists() {
        // given
        given(voterSpiPort.existsByName(VOTER_NAME)).willReturn(true);

        // when
        var thrown = assertThatThrownBy(
                () -> registerVoterService.registerVoter(new RegisterVoterCommand(VOTER_NAME)));

        // then
        thrown.isInstanceOf(VoterAlreadyExistsException.class);
    }

    @Test
    void given_existing_name_when_register_voter_then_does_not_save() {
        // given
        given(voterSpiPort.existsByName(VOTER_NAME)).willReturn(true);

        // when
        assertThatThrownBy(() -> registerVoterService.registerVoter(new RegisterVoterCommand(VOTER_NAME)));

        // then
        then(voterSpiPort).should(never()).save(any(Voter.class));
    }
}
