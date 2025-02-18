package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;

public interface KorrektorMapper {

    Korrektor toDomain(KorrektorEntity korrektorEntity);

    public KorrektorEntity toEntity(Korrektor korrektor);
}
