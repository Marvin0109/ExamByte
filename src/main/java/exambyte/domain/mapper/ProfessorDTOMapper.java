package exambyte.domain.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.aggregate.user.Professor;

public interface ProfessorDTOMapper {

    ProfessorDTO toDTO(Professor professor);
}
