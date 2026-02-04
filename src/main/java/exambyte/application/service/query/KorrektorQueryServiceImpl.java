package exambyte.application.service.query;

import exambyte.application.dto.KorrektorDTO;
import exambyte.domain.mapper.KorrektorDTOMapper;
import exambyte.domain.service.KorrektorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class KorrektorQueryServiceImpl implements KorrektorQueryService {

    private final KorrektorService korrektorService;
    private final KorrektorDTOMapper korrektorDTOMapper;

    public KorrektorQueryServiceImpl(KorrektorService korrektorService,
                                     KorrektorDTOMapper korrektorDTOMapper) {
        this.korrektorService = korrektorService;
        this.korrektorDTOMapper = korrektorDTOMapper;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (korrektorService.getKorrektorByName("Automatischer Korrektor").isEmpty()) {
            korrektorService.saveKorrektor("Automatischer Korrektor");
        }
    }

    @Override
    public UUID getReviewerIdByName(String name) {
        Optional<KorrektorDTO> k = korrektorService.getKorrektorByName(name).map(korrektorDTOMapper::toDTO);
        return k.map(KorrektorDTO::fachId).orElse(null);
    }

    @Override
    public KorrektorDTO getReviewerById(UUID reviewerId) {
        return korrektorDTOMapper.toDTO(korrektorService.getKorrektor(reviewerId));
    }
}
