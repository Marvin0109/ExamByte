package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;

import java.util.UUID;

public class AntwortMapperJDBC {

    public Antwort toDomain(AntwortEntityJDBC antwortEntityJDBC) {

        // Hier muss die FachId des Professors gesucht werden anhand der frageId -> Datenbank Logik
        UUID frageFachId = antwortEntityJDBC.getFachId();

        Frage frage = Frage.of(null, frageFachId, "", null); // Null als UUID for now, Frage Text bleibt erstmal leer
        UUID studentUUID = antwortEntityJDBC.getStudentFachId();

        return Antwort.of(
                antwortEntityJDBC.getId(),
                antwortEntityJDBC.getFachId(),
                antwortEntityJDBC.getAntwortText(),
                antwortEntityJDBC.getIstKorrekt(),
                frage,
                studentUUID
        );
    }

    public AntwortEntityJDBC toEntity(Antwort antwort) {
        UUID frageFachId = antwort.getFrage().getUuid(); // LoD Verstoß
        UUID studentFachId = antwort.getStudentUUID();

        return new AntwortEntityJDBC(
                antwort.getId(),
                antwort.getUuid(),
                antwort.getAntwortText(),
                antwort.getIstKorrekt(),
                frageFachId,
                studentFachId
        );
    }
}
