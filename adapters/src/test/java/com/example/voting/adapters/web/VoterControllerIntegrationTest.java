package com.example.voting.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.voting.adapters.metrics.VotingMetrics;
import com.example.voting.adapters.web.mapper.VoterWebMapperImpl;
import com.example.voting.usecases.api.BlockVoterApiPort;
import com.example.voting.usecases.api.GetVoterApiPort;
import com.example.voting.usecases.api.RegisterVoterApiPort;
import com.example.voting.usecases.api.RegisterVoterCommand;
import com.example.voting.usecases.api.UnblockVoterApiPort;
import com.example.voting.usecases.api.VoterView;
import com.example.voting.usecases.exception.VoterNotFoundException;

@WebMvcTest(VoterController.class)
@Import(VoterWebMapperImpl.class)
class VoterControllerIntegrationTest {

    private static final String VOTERS_PATH = "/api/voters";

    private static final String VOTER_NAME = "Jane Doe";

    private static final String ACTIVE_STATUS = "ACTIVE";

    private static final String REGISTER_VOTER_BODY = "{\"name\":\"Jane Doe\"}";

    private static final String BLANK_NAME_BODY = "{\"name\":\"\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterVoterApiPort registerVoterApiPort;

    @MockBean
    private BlockVoterApiPort blockVoterApiPort;

    @MockBean
    private UnblockVoterApiPort unblockVoterApiPort;

    @MockBean
    private GetVoterApiPort getVoterApiPort;

    @MockBean
    private VotingMetrics votingMetrics;

    @Test
    void given_valid_request_when_register_voter_then_responds_created_with_voter() throws Exception {
        // given
        var voterId = UUID.randomUUID();
        given(registerVoterApiPort.registerVoter(any(RegisterVoterCommand.class)))
                .willReturn(new VoterView(voterId, VOTER_NAME, ACTIVE_STATUS));

        // when
        var result = mockMvc.perform(post(VOTERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REGISTER_VOTER_BODY));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(voterId.toString()))
                .andExpect(jsonPath("$.name").value(VOTER_NAME))
                .andExpect(jsonPath("$.status").value(ACTIVE_STATUS));

        then(votingMetrics).should().recordVoterRegistered();
    }

    @Test
    void given_blank_name_when_register_voter_then_responds_bad_request_with_validation_error() throws Exception {
        // given
        var request = post(VOTERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BLANK_NAME_BODY);

        // when
        var result = mockMvc.perform(request);

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void given_existing_voter_when_get_voter_then_responds_ok_with_voter() throws Exception {
        // given
        var voterId = UUID.randomUUID();
        given(getVoterApiPort.getVoter(voterId))
                .willReturn(new VoterView(voterId, VOTER_NAME, ACTIVE_STATUS));

        // when
        var result = mockMvc.perform(get(VOTERS_PATH + "/" + voterId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voterId.toString()));
    }

    @Test
    void given_unknown_voter_when_get_voter_then_responds_not_found() throws Exception {
        // given
        var voterId = UUID.randomUUID();
        given(getVoterApiPort.getVoter(voterId)).willThrow(new VoterNotFoundException(voterId));

        // when
        var result = mockMvc.perform(get(VOTERS_PATH + "/" + voterId));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void given_active_voter_when_block_voter_then_responds_ok_with_blocked_status() throws Exception {
        // given
        var voterId = UUID.randomUUID();
        given(blockVoterApiPort.blockVoter(voterId))
                .willReturn(new VoterView(voterId, VOTER_NAME, "BLOCKED"));

        // when
        var result = mockMvc.perform(post(VOTERS_PATH + "/" + voterId + "/block"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }
}
