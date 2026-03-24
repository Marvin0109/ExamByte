package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Professor;
import exambyte.infrastructure.entity.ProfessorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapperImpl implements ProfessorMapper {

    @Override
    public Professor toDomain(ProfessorEntity entity) {
        return new Professor.ProfessorBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public ProfessorEntity toEntity(Professor professor) {
        return new ProfessorEntity.ProfessorEntityBuilder()
                .id(professor.id())
                .name(professor.getName())
                .build();
    }
}
