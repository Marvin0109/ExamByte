package exambyte.persistence.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import org.springframework.stereotype.Service;

@Service
public class FrageMapper {

    private final ProfessorMapper professorMapper;

    public FrageMapper(ProfessorMapper professorMapper) {
        this.professorMapper = professorMapper;
    }

    public Frage toDomain(FrageEntityJPA frageEntityJPA) {

        ProfessorEntityJPA professor = frageEntityJPA.getProfessor();
        Professor prof = professorMapper.toDomain(professor);

        return Frage.of(frageEntityJPA.getId(), frageEntityJPA.getFrageText(), prof);
    }

    public FrageEntityJPA toEntity(Frage frage) {

        Professor professor = frage.getProfessor();
        ProfessorEntityJPA professorEntityJPA = professorMapper.toEntity(professor);

        return new FrageEntityJPA(frage.getId(), frage.getFrageText(), professorEntityJPA);
    }
}
