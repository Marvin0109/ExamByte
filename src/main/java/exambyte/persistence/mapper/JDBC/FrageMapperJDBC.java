package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.service.ProfessorService;
import org.springframework.stereotype.Service;

@Service
public class FrageMapperJDBC {

    private final ProfessorMapperJDBC professorMapperJDBC;
    private final ProfessorService professorService;

    public FrageMapperJDBC(ProfessorMapperJDBC professorMapperJDBC, ProfessorService professorService) {
        this.professorMapperJDBC = professorMapperJDBC;
        this.professorService = professorService;
    }

    public Frage toDomain(FrageEntityJDBC frageEntityJDBC) {

        ProfessorEntityJDBC professor = professorService.findProfessorById(frageEntityJDBC.getProfessorId())
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
