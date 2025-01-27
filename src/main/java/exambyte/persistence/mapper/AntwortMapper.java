package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.AntwortEntity;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.StudentEntity;
import org.springframework.stereotype.Service;

@Service
public class AntwortMapper {

    private final StudentMapper studentMapper;
    private final FrageMapper frageMapper;

    public AntwortMapper(StudentMapper studentMapper, FrageMapper frageMapper) {
        this.studentMapper = studentMapper;
        this.frageMapper = frageMapper;
    }

    public Antwort toDomain(AntwortEntity antwortEntity) {

        FrageEntity frageEntity = antwortEntity.getFrage();
        Frage frage = frageMapper.toDomain(frageEntity);

        StudentEntity studentEntity = antwortEntity.getStudent();
        Student student = studentMapper.toDomain(studentEntity);

        return Antwort.of(antwortEntity.getId(), antwortEntity.getAntwortText(), antwortEntity.getIstKorrekt(),
                frage, student);
    }

    public AntwortEntity toEntity(Antwort antwort) {
        Frage frage = antwort.getFrage();
        FrageEntity frageEntity = frageMapper.toEntity(frage);

        Student student = antwort.getStudent();
        StudentEntity studentEntity = studentMapper.toEntity(student);

        return new AntwortEntity(antwort.getId(), antwort.getAntwortText(), antwort.getIstKorrekt(),
                frageEntity, studentEntity);
    }
}
