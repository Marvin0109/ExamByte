package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.entitymapper.AntwortMapper;
import exambyte.infrastructure.persistence.entities.AntwortEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AntwortMapperImpl implements AntwortMapper {

    @Override
    public Antwort toDomain(AntwortEntity antwortEntity) {

        UUID frageFachId = antwortEntity.getFrageFachId();
        UUID studentFachID = antwortEntity.getStudentFachId();

        return new Antwort.AntwortBuilder()
            .id(null)
            .fachId(antwortEntity.getFachId())
            .antwortText(antwortEntity.getAntwortText())
            .frageFachId(frageFachId)
            .studentFachId(studentFachID)
            .antwortZeitpunkt(antwortEntity.getAntwortZeitpunkt())
            .lastChangesZeitpunkt(antwortEntity.getLastChangesZeitpunkt())
            .build();
    }

    @Override
    public AntwortEntity toEntity(Antwort antwort) {
        UUID frageFachId = antwort.getFrageFachId(); // LoD Verstoß
        UUID studentFachId = antwort.getStudentUUID();

        return new AntwortEntity.AntwortEntityBuilder()
                .id(null)
                .fachId(antwort.getFachId())
                .antwortText(antwort.getAntwortText())
                .frageFachId(frageFachId)
                .studentFachId(studentFachId)
                .antwortZeitpunkt(antwort.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(antwort.getLastChangesZeitpunkt())
                .build();
    }
}
