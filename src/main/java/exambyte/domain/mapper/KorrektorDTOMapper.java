package exambyte.domain.mapper;

import exambyte.application.dto.KorrektorDTO;
import exambyte.domain.aggregate.user.Korrektor;

import java.util.List;

public interface KorrektorDTOMapper {

    KorrektorDTO toDTO(Korrektor korrektor);

    List<KorrektorDTO> toKorrektorDTOList(List<Korrektor> korrektoren);
}
