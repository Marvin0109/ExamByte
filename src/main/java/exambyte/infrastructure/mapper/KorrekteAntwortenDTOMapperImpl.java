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
                antworten.getId(),
                antworten.getLoesungen(),
                antworten.getAntwortOptionen(),
                antworten.getFrageId());
    }

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenDTO dto) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(dto.id())
                .frageId(dto.frageId())
                .loesungen(dto.antworten())
                .antwortOptionen(dto.antwortOptionen())
                .build();
    }
}
