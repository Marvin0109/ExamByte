package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.repository.FrageRepositoryImpl;

import java.util.UUID;

public class FrageMapperJDBC {

    private final ProfessorMapperJDBC professorMapperJDBC;
    private final FrageRepositoryImpl frageRepository;

    public FrageMapperJDBC(ProfessorMapperJDBC professorMapperJDBC, FrageRepositoryImpl frageRepository) {
        this.professorMapperJDBC = professorMapperJDBC;
        this.frageRepository = frageRepository;
    }

    public Frage toDomain(FrageEntityJDBC frageEntityJDBC) {

        UUID professorFachID = frageEntityJDBC.getProfessorFachId();
        ProfessorEntityJDBC professor = frageRepository.findByProfFachId(professorFachID);

        Professor prof = professorMapperJDBC.toDomain(professor);

        return Frage.of(frageEntityJDBC.getId(), frageEntityJDBC.getFachId(), frageEntityJDBC.getFrageText(), professorFachID);
    }

    public FrageEntityJDBC toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntityJDBC(frage.getId(), frage.getFachId(), frage.getFrageText(), profUUID);
    }
}
