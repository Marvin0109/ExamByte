package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;

import java.util.UUID;

public class AntwortMapper {

    public static Antwort toDomain(AntwortEntity antwortEntity) {

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
