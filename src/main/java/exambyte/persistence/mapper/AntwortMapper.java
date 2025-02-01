package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;

import java.util.UUID;

public class AntwortMapper {

    public Antwort toDomain(AntwortEntity antwortEntity) {

        UUID frageFachId = antwortEntity.getFrageFachId();
        UUID studentFachID = antwortEntity.getStudentFachId();

        return Antwort.of(
                antwortEntity.getId(),
                antwortEntity.getFachId(),
                antwortEntity.getAntwortText(),
                frageFachId,
                studentFachID,
                antwortEntity.getAntwortZeitpunkt(),
                antwortEntity.getLastChangesZeitpunkt()
        );
    }

    public AntwortEntity toEntity(Antwort antwort) {
        UUID frageFachId = antwort.getFrageFachId(); // LoD Verstoß
        UUID studentFachId = antwort.getStudentUUID();

        return new AntwortEntity(
                antwort.getId(),
                antwort.getFachId(),
                antwort.getAntwortText(),
                frageFachId,
                studentFachId,
                antwort.getAntwortZeitpunkt(),
                antwort.getLastChangesZeitpunkt()
        );
    }
}
