package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.repository.FrageRepositoryImpl;

import java.util.UUID;

public class FrageMapperJDBC {

    private final FrageRepositoryImpl frageRepository;

    public FrageMapperJDBC(FrageRepositoryImpl frageRepository) {
        this.frageRepository = frageRepository;
    }

    public Frage toDomain(FrageEntityJDBC frageEntityJDBC) {

        UUID professorFachID = frageEntityJDBC.getProfessorFachId();
        ProfessorEntityJDBC professor = frageRepository.findByProfFachId(professorFachID);

        return Frage.of(frageEntityJDBC.getId(), frageEntityJDBC.getFachId(), frageEntityJDBC.getFrageText(),
                professorFachID, frageEntityJDBC.getExamFachId());
    }

    public FrageEntityJDBC toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntityJDBC(frage.getId(), frage.getFachId(), frage.getFrageText(), profUUID, frage.getTestUUID());
    }
}
