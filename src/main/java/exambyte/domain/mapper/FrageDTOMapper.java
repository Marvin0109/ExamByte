package exambyte.domain.mapper;

import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Frage;

import java.util.List;

public interface FrageDTOMapper {

    FrageDTO toDTO(Frage frage);

    List<FrageDTO> toFrageDTOList(List<Frage> fragen);
}
