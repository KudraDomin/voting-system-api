package com.example.voting.adapters.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.persistence.jpa.entity.VoteEntity;
import com.example.voting.core.model.Vote;

@Mapper(componentModel = "spring")
public interface VoteEntityMapper extends IdConversions {

    VoteEntity toEntity(Vote vote);

    Vote toDomain(VoteEntity entity);
}
