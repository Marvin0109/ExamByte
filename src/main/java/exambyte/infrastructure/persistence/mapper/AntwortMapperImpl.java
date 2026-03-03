package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.stereotype.Component;

@Component
public class AntwortMapperImpl implements AntwortMapper {

    @Override
    public Antwort toDomain(AntwortEntity entity) {

        return new Antwort.AntwortBuilder()
                .id(entity.getId())
                .antwortText(entity.getAntwortText())
                .frageId(entity.getFrageId())
                .studentId(entity.getStudentId())
                .antwortZeitpunkt(entity.getAntwortZeitpunkt())
                .build();
    }

    @Override
    public AntwortEntity toEntity(Antwort antwort) {

        return new AntwortEntity.AntwortEntityBuilder()
                .id(antwort.getId())
                .antwortText(antwort.getAntwortText())
                .frageId(antwort.getFrageId())
                .studentId(antwort.getStudentUUID())
                .antwortZeitpunkt(antwort.getAntwortZeitpunkt())
                .build();
    }
}
