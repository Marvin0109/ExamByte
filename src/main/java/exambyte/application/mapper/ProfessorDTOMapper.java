package exambyte.application.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.user.Professor;

public interface ProfessorDTOMapper {

    ProfessorDTO toDTO(Professor professor);
}
