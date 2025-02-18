package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.infrastructure.persistence.entities.FrageEntity;

public interface FrageMapper {

    Frage toDomain(FrageEntity frageEntity);

    FrageEntity toEntity(Frage frage);
}
