package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.springframework.stereotype.Component;

@Component
public class KorrekteAntwortenMapperImpl implements KorrekteAntwortenMapper {

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenEntity entity) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(null)
                .fachId(entity.getFachID())
                .frageFachId(entity.getFrageFachID())
                .loesungen(entity.getRichtigeAntwort())
                .antwortOptionen(entity.getAntwortOptionen())
                .build();
    }

    @Override
    public KorrekteAntwortenEntity toEntity(KorrekteAntworten antworten) {
    return new KorrekteAntwortenEntity(
        null,
        antworten.getFachId(),
        antworten.getFrageFachId(),
        antworten.getLoesungen(),
        antworten.getAntwortOptionen());
    }
}
