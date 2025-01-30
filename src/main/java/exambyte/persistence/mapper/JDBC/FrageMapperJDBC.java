package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.repository.ProfessorRepositoryImpl;
import org.springframework.context.annotation.Lazy;

public class FrageMapperJDBC {

    private final ProfessorMapperJDBC professorMapperJDBC;
    private final ProfessorRepositoryImpl professorRepository;

    public FrageMapperJDBC(ProfessorMapperJDBC professorMapperJDBC, @Lazy ProfessorRepositoryImpl professorRepository) {
        this.professorMapperJDBC = professorMapperJDBC;
        this.professorRepository = professorRepository;
    }

    public Frage toDomain(FrageEntityJDBC frageEntityJDBC) {

        ProfessorEntityJDBC professor = professorRepository.findById(frageEntityJDBC.getProfessorId())
                .orElse(new ProfessorEntityJDBC(frageEntityJDBC.getProfessorId(), "Unbekannt"));

        Professor prof = professorMapperJDBC.toDomain(professor);

        return Frage.of(frageEntityJDBC.getId(), frageEntityJDBC.getFrageText(), prof);
    }

    public FrageEntityJDBC toEntity(Frage frage) {

        Professor professor = frage.getProfessor();
        ProfessorEntityJDBC professorEntityJDBC = professorMapperJDBC.toEntity(professor);
        Long professorId = professorEntityJDBC.getId();

        return new FrageEntityJDBC(frage.getId(), frage.getFrageText(), professorId);
    }
}
