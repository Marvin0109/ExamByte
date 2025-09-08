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
                antworten.getFrageFachId(), antworten.getKorrekteAntworten(),
                antworten.getAntwort_optionen());
    }

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenDTO dto) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(dto.getFachID())
                .frageFachId(dto.getFrageFachID())
                .korrekteAntworten(dto.getAntworten())
                .antwort_optionen(dto.getAntwort_optionen())
                .build();
    }
}
