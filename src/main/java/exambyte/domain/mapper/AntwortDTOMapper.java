package exambyte.domain.mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.model.aggregate.exam.Antwort;

import java.util.List;

public interface AntwortDTOMapper {

    AntwortDTO toDTO(Antwort antwort);

    List<AntwortDTO> toAntwortDTOList(List<Antwort> antworten);
}
