package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.infrastructure.persistence.entities.AntwortEntity;

public interface AntwortMapper {

    Antwort toDomain(AntwortEntity antwortEntity);

    AntwortEntity toEntity(Antwort antwort);
}
