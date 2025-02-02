package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;

public class ProfessorMapper {

    public static Professor toDomain(ProfessorEntity professorEntity) {
        return new Professor.ProfessorBuilder()
                .id(null)
                .fachId(professorEntity.getFachId())
                .name(professorEntity.getName())
                .build();
    }

    public ProfessorEntity toEntity(Professor professor) {
        return new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(professor.uuid())
                .name(professor.getName())
                .build();
    }
}
