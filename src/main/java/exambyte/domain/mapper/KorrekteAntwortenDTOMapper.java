package exambyte.domain.mapper;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;

public interface KorrekteAntwortenDTOMapper {

    KorrekteAntwortenDTO toDTO(KorrekteAntworten antworten);
    KorrekteAntworten toDomain(KorrekteAntwortenDTO dto);
}
