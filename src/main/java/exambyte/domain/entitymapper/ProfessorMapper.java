package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;

public interface ProfessorMapper {

    Professor toDomain(ProfessorEntity professorEntity);

    ProfessorEntity toEntity(Professor professor);
}
