package exambyte.application.service.query;

import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.service.KorrekteAntwortenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KorrekteAntwortenQueryServiceImpl implements KorrekteAntwortenQueryService {

    private final KorrekteAntwortenService service;
    private final KorrekteAntwortenDTOMapper mapper;

    public KorrekteAntwortenQueryServiceImpl(KorrekteAntwortenService service,
                                             KorrekteAntwortenDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public KorrekteAntwortenDTO getLoesungForFrage(UUID frageId) {
        KorrekteAntworten k = service.findKorrekteAntwort(frageId);
        return k != null ? mapper.toDTO(k) : null;
    }
}
