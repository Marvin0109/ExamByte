package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.springframework.stereotype.Component;

@Component
public class KorrekteAntwortenDTOMapperImpl implements KorrekteAntwortenDTOMapper {

    @Override
    public KorrekteAntwortenDTO toDTO(KorrekteAntworten antworten) {
        return new KorrekteAntwortenDTO(antworten.getId(), antworten.getFachId(),
                antworten.getFrageFachId(), antworten.getKorrekteAntworten());
    }

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenDTO dto) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(dto.fachID())
                .frageFachId(dto.frageFachID())
                .korrekteAntworten(dto.korrekteAntworten())
                .build();
    }
}
