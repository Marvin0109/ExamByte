package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;

public class ProfessorMapper {

    public static Professor toDomain(ProfessorEntity professorEntity) {
        return Professor.of(professorEntity.getId(), professorEntity.getName());
    }
}
