package exambyte.persistence.mapper.JPA;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import org.springframework.stereotype.Service;

public class ProfessorMapperJPA {

    public Professor toDomain(ProfessorEntityJPA professorEntityJPA) {
        return Professor.of(professorEntityJPA.getId(), professorEntityJPA.getName());
    }

    public ProfessorEntityJPA toEntity(Professor professor) {
        return new ProfessorEntityJPA(professor.getId(), professor.getName());
    }
}
