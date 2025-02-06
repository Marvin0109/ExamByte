package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.persistence.entities.FrageEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FrageMapperImpl implements FrageMapper {

    @Override
    public Frage toDomain(FrageEntity frageEntity) {

        UUID professorFachID = frageEntity.getProfessorFachId();

        return new Frage.FrageBuilder()
                .id(null)
                .fachId(frageEntity.getFachId())
                .frageText(frageEntity.getFrageText())
                .maxPunkte(frageEntity.getMaxPunkte())
                .professorUUID(professorFachID)
                .examUUID(frageEntity.getExamFachId())
                .build();
    }

    @Override
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
