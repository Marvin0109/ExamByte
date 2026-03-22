package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.user.Professor;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;

public interface ProfessorMapper {

    Professor toDomain(ProfessorEntity entity);

    ProfessorEntity toEntity(Professor professor);
}
