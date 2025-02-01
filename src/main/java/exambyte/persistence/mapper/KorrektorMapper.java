package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.persistence.entities.KorrektorEntity;

public class KorrektorMapper {

    public Korrektor toDomain(KorrektorEntity korrektorEntity) {
        return Korrektor.of(korrektorEntity.getId(), korrektorEntity.getFachId(), korrektorEntity.getName());
    }

    public KorrektorEntity toEntity(Korrektor korrektor) {
        return new KorrektorEntity(korrektor.getId(), korrektor.uuid(), korrektor.getName());
    }
}
