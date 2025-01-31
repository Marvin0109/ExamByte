package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.repository.AntwortRepositoryImpl;

import java.util.UUID;

public class AntwortMapperJDBC {

    public Antwort toDomain(AntwortEntityJDBC antwortEntityJDBC) {

        UUID frageFachId = antwortEntityJDBC.getFrageFachId();
        UUID studentFachID = antwortEntityJDBC.getStudentFachId();

        return Antwort.of(
                antwortEntityJDBC.getId(),
                antwortEntityJDBC.getFachId(),
                antwortEntityJDBC.getAntwortText(),
                antwortEntityJDBC.getIstKorrekt(),
                frageFachId,
                studentFachID
        );
    }

    public AntwortEntityJDBC toEntity(Antwort antwort) {
        UUID frageFachId = antwort.getFrageFachId(); // LoD Verstoß
        UUID studentFachId = antwort.getStudentUUID();

        return new AntwortEntityJDBC(
                antwort.getId(),
                antwort.getFachId(),
                antwort.getAntwortText(),
                antwort.getIstKorrekt(),
                frageFachId,
                studentFachId
        );
    }
}
