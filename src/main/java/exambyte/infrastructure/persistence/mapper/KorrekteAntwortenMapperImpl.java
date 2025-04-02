package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.entitymapper.KorrekteAntwortenMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KorrekteAntwortenMapperImpl implements KorrekteAntwortenMapper {

    @Override
    public KorrekteAntworten toDomain(KorrekteAntwortenEntity entity) {
        return new KorrekteAntworten.KorrekteAntwortenBuilder()
                .fachId(entity.getFachID())
                .frageFachId(entity.getFrageFachID())
                .korrekteAntworten(entity.getRichtigeAntwort())
                .build();
    }

    @Override
    public KorrekteAntwortenEntity toEntity(KorrekteAntworten antworten) {
    return new KorrekteAntwortenEntity(
        antworten.getId(),
        antworten.getFachId(),
        antworten.getFrageFachId(),
        List.of(antworten.getKorrekteAntworten()));
    }
}
