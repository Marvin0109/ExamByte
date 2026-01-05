package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import org.springframework.stereotype.Component;

@Component
public class KorrektorMapperImpl implements KorrektorMapper {

    @Override
    public Korrektor toDomain(KorrektorEntity entity) {
        return new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(entity.getFachId())
                .name(entity.getName())
                .build();
    }

    @Override
    public KorrektorEntity toEntity(Korrektor korrektor) {
        return new KorrektorEntity.KorrektorEntityBuilder()
                .id(null)
                .fachId(korrektor.uuid())
                .name(korrektor.getName())
                .build();
    }
}
