package com.example.voting.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.voting.adapters.web.mapper.ElectionWebMapperImpl;
import com.example.voting.usecases.api.AddElectionOptionApiPort;
import com.example.voting.usecases.api.AddElectionOptionCommand;
import com.example.voting.usecases.api.CreateElectionApiPort;
import com.example.voting.usecases.api.CreateElectionCommand;
import com.example.voting.usecases.api.ElectionOptionView;
import com.example.voting.usecases.api.ElectionView;
import com.example.voting.usecases.api.GetElectionApiPort;
import com.example.voting.usecases.exception.ElectionNotFoundException;

@WebMvcTest(ElectionController.class)
@Import(ElectionWebMapperImpl.class)
class ElectionControllerIntegrationTest {

    private static final String ELECTIONS_PATH = "/api/elections";

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    private static final String OPTION_LABEL = "Candidate A";

    private static final String CREATE_ELECTION_BODY = "{\"title\":\"Mayor Election 2025\"}";

    private static final String BLANK_TITLE_BODY = "{\"title\":\"\"}";

    private static final String ADD_OPTION_BODY = "{\"label\":\"Candidate A\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateElectionApiPort createElectionApiPort;

    @MockBean
    private AddElectionOptionApiPort addElectionOptionApiPort;

    @MockBean
    private GetElectionApiPort getElectionApiPort;

    @Test
    void given_valid_request_when_create_election_then_responds_created_with_election() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        given(createElectionApiPort.createElection(any(CreateElectionCommand.class)))
                .willReturn(new ElectionView(electionId, ELECTION_TITLE, List.of()));

        // when
        var result = mockMvc.perform(post(ELECTIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_ELECTION_BODY));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(electionId.toString()))
                .andExpect(jsonPath("$.title").value(ELECTION_TITLE))
                .andExpect(jsonPath("$.options").isEmpty());
    }

    @Test
    void given_blank_title_when_create_election_then_responds_bad_request() throws Exception {
        // given
        var request = post(ELECTIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BLANK_TITLE_BODY);

        // when
        var result = mockMvc.perform(request);

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void given_existing_election_when_get_election_then_responds_ok_with_options() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        given(getElectionApiPort.getElection(electionId))
                .willReturn(new ElectionView(
                        electionId,
                        ELECTION_TITLE,
                        List.of(new ElectionOptionView(optionId, OPTION_LABEL))));

        // when
        var result = mockMvc.perform(get(ELECTIONS_PATH + "/" + electionId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.options[0].id").value(optionId.toString()))
                .andExpect(jsonPath("$.options[0].label").value(OPTION_LABEL));
    }

    @Test
    void given_unknown_election_when_get_election_then_responds_not_found() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        given(getElectionApiPort.getElection(electionId)).willThrow(new ElectionNotFoundException(electionId));

        // when
        var result = mockMvc.perform(get(ELECTIONS_PATH + "/" + electionId));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void given_valid_request_when_add_option_then_responds_created_with_option() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        given(addElectionOptionApiPort.addOption(any(AddElectionOptionCommand.class)))
                .willReturn(new ElectionView(
                        electionId,
                        ELECTION_TITLE,
                        List.of(new ElectionOptionView(optionId, OPTION_LABEL))));

        // when
        var result = mockMvc.perform(post(ELECTIONS_PATH + "/" + electionId + "/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ADD_OPTION_BODY));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.options[0].label").value(OPTION_LABEL));
    }
}
