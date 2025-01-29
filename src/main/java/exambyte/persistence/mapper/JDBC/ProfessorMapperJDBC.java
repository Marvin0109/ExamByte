package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;

public class ProfessorMapperJDBC {

    public Professor toDomain(ProfessorEntityJDBC professorEntityJDBC) {
        return Professor.of(professorEntityJDBC.getId(), professorEntityJDBC.getName());
    }

    public ProfessorEntityJDBC toEntity(Professor professor) {
        return new ProfessorEntityJDBC(professor.getId(), professor.getName());
    }
}
