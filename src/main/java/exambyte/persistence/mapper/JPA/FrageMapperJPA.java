package exambyte.persistence.mapper.JPA;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.JPA.FrageEntityJPA;
import exambyte.persistence.entities.JPA.ProfessorEntityJPA;
import org.springframework.stereotype.Service;

@Service
public class FrageMapperJPA {

    private final ProfessorMapperJPA professorMapperJPA;

    public FrageMapperJPA(ProfessorMapperJPA professorMapperJPA) {
        this.professorMapperJPA = professorMapperJPA;
    }

    public Frage toDomain(FrageEntityJPA frageEntityJPA) {

        ProfessorEntityJPA professor = frageEntityJPA.getProfessor();
        Professor prof = professorMapperJPA.toDomain(professor);

        return Frage.of(frageEntityJPA.getId(), frageEntityJPA.getFrageText(), prof);
    }

    public FrageEntityJPA toEntity(Frage frage) {

        Professor professor = frage.getProfessor();
        ProfessorEntityJPA professorEntityJPA = professorMapperJPA.toEntity(professor);

        return new FrageEntityJPA(frage.getId(), frage.getFrageText(), professorEntityJPA);
    }
}
