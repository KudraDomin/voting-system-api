package com.example.voting.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.voting.adapters.metrics.VotingMetrics;
import com.example.voting.adapters.web.mapper.VoteWebMapperImpl;
import com.example.voting.core.exception.BlockedVoterCannotVoteException;
import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.api.CastVoteApiPort;
import com.example.voting.usecases.api.CastVoteCommand;
import com.example.voting.usecases.api.VoteView;

@WebMvcTest(VoteController.class)
@Import(VoteWebMapperImpl.class)
class VoteControllerIntegrationTest {

    private static final String VOTES_PATH_TEMPLATE = "/api/elections/%s/votes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CastVoteApiPort castVoteApiPort;

    @MockBean
    private VotingMetrics votingMetrics;

    @BeforeEach
    void setUp() {
        given(votingMetrics.recordCastVote(any())).willAnswer(invocation -> {
            Supplier<?> operation = invocation.getArgument(0);

            return operation.get();
        });
    }

    @Test
    void given_valid_request_when_cast_vote_then_responds_created_with_vote() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var voterId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        var voteId = UUID.randomUUID();
        given(castVoteApiPort.castVote(any(CastVoteCommand.class)))
                .willReturn(new VoteView(voteId, electionId, voterId, optionId));

        // when
        var result = mockMvc.perform(post(votesPath(electionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(castVoteBody(voterId, optionId)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(voteId.toString()))
                .andExpect(jsonPath("$.optionId").value(optionId.toString()));
    }

    @Test
    void given_missing_voter_id_when_cast_vote_then_responds_bad_request() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var body = "{\"optionId\":\"" + UUID.randomUUID() + "\"}";

        // when
        var result = mockMvc.perform(post(votesPath(electionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    void given_voter_already_voted_when_cast_vote_then_responds_conflict_with_duplicate_vote() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var voterId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        given(castVoteApiPort.castVote(any(CastVoteCommand.class)))
                .willThrow(new DuplicateVoteException(VoterId.of(voterId), ElectionId.of(electionId)));

        // when
        var result = mockMvc.perform(post(votesPath(electionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(castVoteBody(voterId, optionId)));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_VOTE"));
    }

    @Test
    void given_blocked_voter_when_cast_vote_then_responds_conflict_with_voter_blocked() throws Exception {
        // given
        var electionId = UUID.randomUUID();
        var voterId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        given(castVoteApiPort.castVote(any(CastVoteCommand.class)))
                .willThrow(new BlockedVoterCannotVoteException(VoterId.of(voterId)));

        // when
        var result = mockMvc.perform(post(votesPath(electionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(castVoteBody(voterId, optionId)));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("VOTER_BLOCKED"));
    }

    private String votesPath(UUID electionId) {
        return VOTES_PATH_TEMPLATE.formatted(electionId);
    }

    private String castVoteBody(UUID voterId, UUID optionId) {
        return "{\"voterId\":\"" + voterId + "\",\"optionId\":\"" + optionId + "\"}";
    }
}
