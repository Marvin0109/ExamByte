package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;

public interface FrageMapper {

    Frage toDomain(FrageEntity frageEntity);

    FrageEntity toEntity(Frage frage);
}
