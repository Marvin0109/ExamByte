package exambyte.persistence.mapper.JPA;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JPA.AntwortEntityJPA;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.StudentEntityJPA;
import org.springframework.stereotype.Service;

@Service
public class AntwortMapperJPA {

    private final StudentMapperJPA studentMapperJPA;
    private final FrageMapperJPA frageMapperJPA;

    public AntwortMapperJPA(StudentMapperJPA studentMapperJPA, FrageMapperJPA frageMapperJPA) {
        this.studentMapperJPA = studentMapperJPA;
        this.frageMapperJPA = frageMapperJPA;
    }

    public Antwort toDomain(AntwortEntityJPA antwortEntityJPA) {

        FrageEntityJPA frageEntityJPA = antwortEntityJPA.getFrage();
        Frage frage = frageMapperJPA.toDomain(frageEntityJPA);

        StudentEntityJPA studentEntityJPA = antwortEntityJPA.getStudent();
        Student student = studentMapperJPA.toDomain(studentEntityJPA);

        return Antwort.of(antwortEntityJPA.getId(), antwortEntityJPA.getAntwortText(), antwortEntityJPA.getIstKorrekt(),
                frage, student);
    }

    public AntwortEntityJPA toEntity(Antwort antwort) {
        Frage frage = antwort.getFrage();
        FrageEntityJPA frageEntityJPA = frageMapperJPA.toEntity(frage);

        Student student = antwort.getStudent();
        StudentEntityJPA studentEntityJPA = studentMapperJPA.toEntity(student);

        return new AntwortEntityJPA(antwort.getId(), antwort.getAntwortText(), antwort.getIstKorrekt(),
                frageEntityJPA, studentEntityJPA);
    }
}
