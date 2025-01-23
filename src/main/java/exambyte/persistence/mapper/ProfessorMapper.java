package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.security.core.parameters.P;

public class ProfessorMapper {

    public static Professor toDomain(ProfessorEntity professorEntity) {
        return Professor.of(professorEntity.getId(), professorEntity.getName());
    }
    public static ProfessorEntity toEntity(Professor professor) {
        return new ProfessorEntity(professor.getId(), professor.getName());
    }
}
