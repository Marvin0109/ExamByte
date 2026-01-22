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
                antworten.getFrageFachId(), antworten.getLoesungen(),
                antworten.getAntwortOptionen());
    }

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenDTO dto) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(dto.getFachID())
                .frageFachId(dto.getFrageFachID())
                .loesungen(dto.getAntworten())
                .antwortOptionen(dto.getAntwort_optionen())
                .build();
    }
}
