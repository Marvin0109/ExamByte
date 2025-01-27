package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.stereotype.Service;

@Service
public class FrageMapper {

    private final ProfessorMapper professorMapper;

    public FrageMapper(ProfessorMapper professorMapper) {
        this.professorMapper = professorMapper;
    }

    public Frage toDomain(FrageEntity frageEntity) {

        ProfessorEntity professor = frageEntity.getProfessor();
        Professor prof = professorMapper.toDomain(professor);

        return Frage.of(frageEntity.getId(), frageEntity.getFrageText(), prof);
    }

    public FrageEntity toEntity(Frage frage) {

        Professor professor = frage.getProfessor();
        ProfessorEntity professorEntity = professorMapper.toEntity(professor);

        return new FrageEntity(frage.getId(), frage.getFrageText(), professorEntity);
    }
}
