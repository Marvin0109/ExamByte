package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.persistence.entities.KorrektorEntity;

public class KorrektorMapper {

    public Korrektor toDomain(KorrektorEntity korrektorEntity) {
        return new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(korrektorEntity.getFachId())
                .name(korrektorEntity.getName())
                .build();
    }

    public KorrektorEntity toEntity(Korrektor korrektor) {
        return new KorrektorEntity.KorrektorEntityBuilder()
                .id(null)
                .fachId(korrektor.uuid())
                .name(korrektor.getName())
                .build();
    }
}
