package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.persistence.entities.ProfessorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapperImpl implements ProfessorMapper {

    @Override
    public Professor toDomain(ProfessorEntity professorEntity) {
        return new Professor.ProfessorBuilder()
                .id(null)
                .fachId(professorEntity.getFachId())
                .name(professorEntity.getName())
                .build();
    }

    @Override
    public ProfessorEntity toEntity(Professor professor) {
        return new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(professor.uuid())
                .name(professor.getName())
                .build();
    }
}
