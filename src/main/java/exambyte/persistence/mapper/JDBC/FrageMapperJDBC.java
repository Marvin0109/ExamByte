package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.repository.ProfessorRepositoryImpl;
import org.springframework.context.annotation.Lazy;

import java.util.UUID;

public class FrageMapperJDBC {

    private final ProfessorMapperJDBC professorMapperJDBC;
    private final ProfessorRepositoryImpl professorRepository;

    public FrageMapperJDBC(ProfessorMapperJDBC professorMapperJDBC, @Lazy ProfessorRepositoryImpl professorRepository) {
        this.professorMapperJDBC = professorMapperJDBC;
        this.professorRepository = professorRepository;
    }

    public Frage toDomain(FrageEntityJDBC frageEntityJDBC) {

        ProfessorEntityJDBC professor = professorRepository.findByFachId(frageEntityJDBC.getProfessorFachId())
                .orElse(new ProfessorEntityJDBC(null, frageEntityJDBC.getProfessorFachId(), "Unbekannt"));

        Professor prof = professorMapperJDBC.toDomain(professor);
        UUID profUUID = prof.uuid();

        return Frage.of(frageEntityJDBC.getId(), frageEntityJDBC.getFachId(), frageEntityJDBC.getFrageText(), profUUID);
    }

    public FrageEntityJDBC toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntityJDBC(frage.getId(), frage.getUuid(), frage.getFrageText(), profUUID);
    }
}
