package exambyte.infrastructure.mapper;

import exambyte.application.dto.KorrektorDTO;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.mapper.KorrektorDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KorrektorDTOMapperImpl implements KorrektorDTOMapper {

    @Override
    public KorrektorDTO toDTO(Korrektor korrektor) {
        return new KorrektorDTO(
                null,
                korrektor.uuid(),
                korrektor.getName());
    }

    @Override
    public List<KorrektorDTO> toKorrektorDTOList(List<Korrektor> korrektoren) {
        return korrektoren.stream()
                .map(this::toDTO)
                .toList();
    }
}
