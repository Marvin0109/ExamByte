package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfessorMapper {

    public Professor toDomain(ProfessorEntity professorEntity) {
        return Professor.of(professorEntity.getId(), professorEntity.getName());
    }

    public ProfessorEntity toEntity(Professor professor) {
        return new ProfessorEntity(professor.getId(), professor.getName());
    }
}
