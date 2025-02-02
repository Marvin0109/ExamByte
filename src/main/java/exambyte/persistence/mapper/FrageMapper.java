package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.repository.FrageRepositoryImpl;

import java.util.UUID;

public class FrageMapper {

    private final FrageRepositoryImpl frageRepository;

    public FrageMapper(FrageRepositoryImpl frageRepository) {
        this.frageRepository = frageRepository;
    }

    public Frage toDomain(FrageEntity frageEntity) {

        UUID professorFachID = frageEntity.getProfessorFachId();
        ProfessorEntity professor = frageRepository.findByProfFachId(professorFachID);

        return new Frage.FrageBuilder()
                .id(null)
                .fachId(frageEntity.getFachId())
                .frageText(frageEntity.getFrageText())
                .maxPunkte(frageEntity.getMaxPunkte())
                .professorUUID(professorFachID)
                .examUUID(frageEntity.getExamFachId())
                .build();
    }

    public FrageEntity toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntity.FrageEntityBuilder()
                .id(null)
                .fachId(frage.getFachId())
                .frageText(frage.getFrageText())
                .maxPunkte(frage.getMaxPunkte())
                .professorFachId(profUUID)
                .examFachId(frage.getExamUUID())
                .build();
    }
}
