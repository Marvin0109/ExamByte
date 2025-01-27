package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JPA.AntwortEntityJPA;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.StudentEntityJPA;
import org.springframework.stereotype.Service;

@Service
public class AntwortMapper {

    private final StudentMapper studentMapper;
    private final FrageMapper frageMapper;

    public AntwortMapper(StudentMapper studentMapper, FrageMapper frageMapper) {
        this.studentMapper = studentMapper;
        this.frageMapper = frageMapper;
    }

    public Antwort toDomain(AntwortEntityJPA antwortEntityJPA) {

        FrageEntityJPA frageEntityJPA = antwortEntityJPA.getFrage();
        Frage frage = frageMapper.toDomain(frageEntityJPA);

        StudentEntityJPA studentEntityJPA = antwortEntityJPA.getStudent();
        Student student = studentMapper.toDomain(studentEntityJPA);

        return Antwort.of(antwortEntityJPA.getId(), antwortEntityJPA.getAntwortText(), antwortEntityJPA.getIstKorrekt(),
                frage, student);
    }

    public AntwortEntityJPA toEntity(Antwort antwort) {
        Frage frage = antwort.getFrage();
        FrageEntityJPA frageEntityJPA = frageMapper.toEntity(frage);

        Student student = antwort.getStudent();
        StudentEntityJPA studentEntityJPA = studentMapper.toEntity(student);

        return new AntwortEntityJPA(antwort.getId(), antwort.getAntwortText(), antwort.getIstKorrekt(),
                frageEntityJPA, studentEntityJPA);
    }
}
