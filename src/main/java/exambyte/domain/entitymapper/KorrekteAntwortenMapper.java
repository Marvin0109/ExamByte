package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.infrastructure.persistence.entities.KorrekteAntwortenEntity;

public interface KorrekteAntwortenMapper {

    KorrekteAntworten toDomain(KorrekteAntwortenEntity entity);

    KorrekteAntwortenEntity toEntity(KorrekteAntworten antworten);
}
