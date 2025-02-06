package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.persistence.entities.KorrektorEntity;
import org.springframework.stereotype.Component;

@Component
public class KorrektorMapperImpl implements KorrektorMapper {

    @Override
    public Korrektor toDomain(KorrektorEntity korrektorEntity) {
        return new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(korrektorEntity.getFachId())
                .name(korrektorEntity.getName())
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
