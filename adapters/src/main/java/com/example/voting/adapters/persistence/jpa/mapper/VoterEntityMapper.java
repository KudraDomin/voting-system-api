package com.example.voting.adapters.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.persistence.jpa.entity.VoterEntity;
import com.example.voting.core.model.Voter;

@Mapper(componentModel = "spring")
public interface VoterEntityMapper extends IdConversions {

    default VoterEntity toEntity(Voter voter) {
        if (voter == null) {
            return null;
        }

        var entity = new VoterEntity();
        entity.setId(fromVoterId(voter.id()));
        entity.setName(voter.name());
        entity.setStatus(voter.status().name());

        return entity;
    }

    Voter toDomain(VoterEntity entity);
}
