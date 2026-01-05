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
            .id(null)
            .fachId(entity.getFachId())
            .antwortText(entity.getAntwortText())
            .frageFachId(entity.getFrageFachId())
            .studentFachId(entity.getStudentFachId())
            .antwortZeitpunkt(entity.getAntwortZeitpunkt())
            .lastChangesZeitpunkt(entity.getLastChangesZeitpunkt())
            .build();
    }

    @Override
    public AntwortEntity toEntity(Antwort antwort) {

        return new AntwortEntity.AntwortEntityBuilder()
                .id(null)
                .fachId(antwort.getFachId())
                .antwortText(antwort.getAntwortText())
                .frageFachId(antwort.getFrageFachId())
                .studentFachId(antwort.getStudentUUID())
                .antwortZeitpunkt(antwort.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(antwort.getLastChangesZeitpunkt())
                .build();
    }
}
