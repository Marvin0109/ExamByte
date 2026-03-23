package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.user.Professor;
import exambyte.infrastructure.entity.ProfessorEntity;

public interface ProfessorMapper {

    Professor toDomain(ProfessorEntity entity);

    ProfessorEntity toEntity(Professor professor);
}
