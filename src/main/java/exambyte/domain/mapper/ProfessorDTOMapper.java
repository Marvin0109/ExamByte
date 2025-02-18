package exambyte.domain.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.aggregate.user.Professor;

import java.util.List;

public interface ProfessorDTOMapper {

    ProfessorDTO toDTO(Professor professor);

    List<ProfessorDTO> toProfessorDTOList(List<Professor> professoren);
}
