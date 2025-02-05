package exambyte.service.mapper;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.service.dto.KorrektorDTO;

public class KorrektorMapperDTO {

    public static KorrektorDTO toDTO(Korrektor korrektor) {
        return new KorrektorDTO(null, korrektor.uuid(), korrektor.getName());
    }

    public static Korrektor toDomain(KorrektorDTO dto) {
        return new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(dto.fachId())
                .name(dto.name())
                .build();
    }
}
