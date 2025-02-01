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

        return Frage.of(
                frageEntity.getId(),
                frageEntity.getFachId(),
                frageEntity.getFrageText(),
                frageEntity.getMaxPunkte(),
                professorFachID,
                frageEntity.getExamFachId());
    }

    public FrageEntity toEntity(Frage frage) {

        UUID profUUID = frage.getProfessorUUID();

        return new FrageEntity(
                frage.getId(),
                frage.getFachId(),
                frage.getFrageText(),
                frage.getMaxPunkte(),
                profUUID,
                frage.getExamUUID());
    }
}
