package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import org.springframework.stereotype.Component;

@Component
public class KorrekteAntwortenDTOMapperImpl implements KorrekteAntwortenDTOMapper {

    @Override
    public KorrekteAntwortenDTO toDTO(KorrekteAntworten antworten) {
        return new KorrekteAntwortenDTO(
                antworten.getFachId(),
                antworten.getLoesungen(),
                antworten.getAntwortOptionen(),
                antworten.getFrageFachId());
    }

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenDTO dto) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(dto.fachId())
                .frageFachId(dto.frageFachId())
                .loesungen(dto.antworten())
                .antwortOptionen(dto.antwortOptionen())
                .build();
    }
}
