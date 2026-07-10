package com.example.voting.usecases.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.voting.core.exception.BlockedVoterCannotVoteException;
import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.exception.OptionNotInElectionException;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.Vote;
import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.core.service.VoteFactory;
import com.example.voting.usecases.api.AddElectionOptionCommand;
import com.example.voting.usecases.api.CastVoteCommand;
import com.example.voting.usecases.api.CreateElectionCommand;
import com.example.voting.usecases.api.ElectionView;
import com.example.voting.usecases.api.RegisterVoterCommand;
import com.example.voting.usecases.api.VoterView;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.service.AddElectionOptionService;
import com.example.voting.usecases.service.CastVoteService;
import com.example.voting.usecases.service.CreateElectionService;
import com.example.voting.usecases.service.ManageVoterStatusService;
import com.example.voting.usecases.service.QueryElectionService;
import com.example.voting.usecases.service.RegisterVoterService;
import com.example.voting.usecases.spi.ElectionSpiPort;
import com.example.voting.usecases.spi.VoteSpiPort;
import com.example.voting.usecases.spi.VoterSpiPort;

class VotingWorkflowUseCaseIntegrationTest {

    private static final String VOTER_NAME = "Jane Doe";

    private static final String SECOND_VOTER_NAME = "John Roe";

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    private static final String OPTION_LABEL = "Candidate A";

    private RegisterVoterService registerVoterService;

    private ManageVoterStatusService manageVoterStatusService;

    private CreateElectionService createElectionService;

    private AddElectionOptionService addElectionOptionService;

    private CastVoteService castVoteService;

    private QueryElectionService queryElectionService;

    @BeforeEach
    void setUp() {
        var voterRepository = new InMemoryVoterRepository();
        var electionRepository = new InMemoryElectionRepository();
        var voteRepository = new InMemoryVoteRepository();

        registerVoterService = new RegisterVoterService(voterRepository);
        manageVoterStatusService = new ManageVoterStatusService(voterRepository);
        createElectionService = new CreateElectionService(electionRepository);
        addElectionOptionService = new AddElectionOptionService(electionRepository);
        castVoteService = new CastVoteService(voterRepository, electionRepository, voteRepository, new VoteFactory());
        queryElectionService = new QueryElectionService(electionRepository);
    }

    @Test
    void given_registered_voter_and_election_with_option_when_cast_vote_then_vote_targets_chosen_option() {
        // given
        var voter = registerVoter(VOTER_NAME);
        var election = createElectionWithOption();
        var optionId = firstOptionId(election);

        // when
        var vote = castVoteService.castVote(new CastVoteCommand(election.id(), voter.id(), optionId));

        // then
        assertThat(vote.optionId()).isEqualTo(optionId);
        assertThat(vote.voterId()).isEqualTo(voter.id());
        assertThat(vote.electionId()).isEqualTo(election.id());
    }

    @Test
    void given_added_option_when_query_election_then_option_is_visible_across_services() {
        // given
        var election = createElectionWithOption();

        // when
        var reloaded = queryElectionService.getElection(election.id());

        // then
        assertThat(reloaded.options())
                .singleElement()
                .satisfies(option -> assertThat(option.label()).isEqualTo(OPTION_LABEL));
    }

    @Test
    void given_voter_already_voted_when_cast_vote_again_then_throws_duplicate_vote() {
        // given
        var voter = registerVoter(VOTER_NAME);
        var election = createElectionWithOption();
        var optionId = firstOptionId(election);
        castVoteService.castVote(new CastVoteCommand(election.id(), voter.id(), optionId));

        // when
        var thrown = assertThatThrownBy(
                () -> castVoteService.castVote(new CastVoteCommand(election.id(), voter.id(), optionId)));

        // then
        thrown.isInstanceOf(DuplicateVoteException.class);
    }

    @Test
    void given_blocked_voter_when_cast_vote_then_throws_blocked_voter_cannot_vote() {
        // given
        var voter = registerVoter(SECOND_VOTER_NAME);
        manageVoterStatusService.blockVoter(voter.id());
        var election = createElectionWithOption();
        var optionId = firstOptionId(election);

        // when
        var thrown = assertThatThrownBy(
                () -> castVoteService.castVote(new CastVoteCommand(election.id(), voter.id(), optionId)));

        // then
        thrown.isInstanceOf(BlockedVoterCannotVoteException.class);
    }

    @Test
    void given_option_from_other_election_when_cast_vote_then_throws_option_not_in_election() {
        // given
        var voter = registerVoter(VOTER_NAME);
        var election = createElectionWithOption();
        var foreignOptionId = UUID.randomUUID();

        // when
        var thrown = assertThatThrownBy(
                () -> castVoteService.castVote(new CastVoteCommand(election.id(), voter.id(), foreignOptionId)));

        // then
        thrown.isInstanceOf(OptionNotInElectionException.class);
    }

    @Test
    void given_unknown_election_when_cast_vote_then_throws_election_not_found() {
        // given
        var voter = registerVoter(VOTER_NAME);
        var unknownElectionId = UUID.randomUUID();

        // when
        var thrown = assertThatThrownBy(
                () -> castVoteService.castVote(new CastVoteCommand(unknownElectionId, voter.id(), UUID.randomUUID())));

        // then
        thrown.isInstanceOf(ElectionNotFoundException.class);
    }

    private VoterView registerVoter(String name) {
        return registerVoterService.registerVoter(new RegisterVoterCommand(name));
    }

    private ElectionView createElectionWithOption() {
        var created = createElectionService.createElection(new CreateElectionCommand(ELECTION_TITLE));

        return addElectionOptionService.addOption(new AddElectionOptionCommand(created.id(), OPTION_LABEL));
    }

    private UUID firstOptionId(ElectionView election) {
        return election.options().get(0).id();
    }

    private static final class InMemoryVoterRepository implements VoterSpiPort {

        private final Map<UUID, Voter> votersById = new HashMap<>();

        @Override
        public Voter save(Voter voter) {
            votersById.put(voter.id().value(), voter);

            return voter;
        }

        @Override
        public Optional<Voter> findById(VoterId voterId) {
            return Optional.ofNullable(votersById.get(voterId.value()));
        }

        @Override
        public boolean existsByName(String name) {
            return votersById.values().stream()
                    .anyMatch(voter -> voter.name().equals(name));
        }
    }

    private static final class InMemoryElectionRepository implements ElectionSpiPort {

        private final Map<UUID, Election> electionsById = new HashMap<>();

        @Override
        public Election save(Election election) {
            electionsById.put(election.id().value(), election);

            return election;
        }

        @Override
        public Optional<Election> findById(ElectionId electionId) {
            return Optional.ofNullable(electionsById.get(electionId.value()));
        }
    }

    private static final class InMemoryVoteRepository implements VoteSpiPort {

        private final List<Vote> votes = new ArrayList<>();

        @Override
        public Vote save(Vote vote) {
            votes.add(vote);

            return vote;
        }

        @Override
        public boolean existsByVoterAndElection(VoterId voterId, ElectionId electionId) {
            return votes.stream()
                    .anyMatch(vote -> vote.voterId().equals(voterId) && vote.electionId().equals(electionId));
        }
    }
}
