package com.example.voting.adapters.persistence.jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.voting.adapters.persistence.jpa.entity.ElectionEntity;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionOption;

@Mapper(componentModel = "spring")
public interface ElectionEntityMapper extends IdConversions {

    default ElectionEntity toEntity(Election election) {
        if (election == null) {
            return null;
        }

        var entity = new ElectionEntity();
        entity.setId(fromElectionId(election.id()));
        entity.setTitle(election.title());

        return entity;
    }

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "title", source = "entity.title")
    @Mapping(target = "options", source = "options")
    Election toDomain(ElectionEntity entity, List<ElectionOption> options);
}
