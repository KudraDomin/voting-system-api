package com.example.voting.adapters.metrics;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Component
public class VotingMetrics {

    private static final String VOTERS_REGISTERED_METRIC = "voting.voters.registered";

    private static final String VOTES_CAST_METRIC = "voting.votes.cast";

    private static final String CAST_VOTE_DURATION_METRIC = "voting.votes.cast.duration";

    private final Counter votersRegisteredCounter;

    private final Counter votesCastCounter;

    private final Timer castVoteTimer;

    public VotingMetrics(MeterRegistry meterRegistry) {
        this.votersRegisteredCounter = Counter.builder(VOTERS_REGISTERED_METRIC)
                .description("Total number of registered voters")
                .register(meterRegistry);

        this.votesCastCounter = Counter.builder(VOTES_CAST_METRIC)
                .description("Total number of votes cast")
                .register(meterRegistry);

        this.castVoteTimer = Timer.builder(CAST_VOTE_DURATION_METRIC)
                .description("Duration of cast vote operations")
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void recordVoterRegistered() {
        votersRegisteredCounter.increment();
    }

    public <T> T recordCastVote(Supplier<T> castVoteOperation) {
        var result = castVoteTimer.record(castVoteOperation);
        votesCastCounter.increment();

        return result;
    }
}
