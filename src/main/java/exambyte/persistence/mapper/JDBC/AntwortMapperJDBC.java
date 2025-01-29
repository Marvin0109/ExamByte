package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;

public class AntwortMapperJDBC {

    public Antwort toDomain(AntwortEntityJDBC antwortEntityJDBC) {
        Long frageId = antwortEntityJDBC.getFrageId();
        Long studentId = antwortEntityJDBC.getStudentId();

        Frage frage = Frage.of(frageId, "", Professor.of(0L, ""));
        Student student = Student.of(studentId, "");

        return Antwort.of(
                antwortEntityJDBC.getId(),
                antwortEntityJDBC.getAntwortText(),
                antwortEntityJDBC.getIstKorrekt(),
                frage,
                student
        );
    }

    public AntwortEntityJDBC toEntity(Antwort antwort) {
        Long frageId = antwort.getFrage().getId();
        Long studentId = antwort.getStudent().getId();

        return new AntwortEntityJDBC(
                antwort.getId(),
                antwort.getAntwortText(),
                antwort.getIstKorrekt(),
                frageId,
                studentId
        );
    }
}
