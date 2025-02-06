package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;

public interface AntwortMapper {

    Antwort toDomain(AntwortEntity antwortEntity);

    AntwortEntity toEntity(Antwort antwort);
}
