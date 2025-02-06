package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.persistence.entities.KorrektorEntity;

public interface KorrektorMapper {

    Korrektor toDomain(KorrektorEntity korrektorEntity);

    public KorrektorEntity toEntity(Korrektor korrektor);
}
