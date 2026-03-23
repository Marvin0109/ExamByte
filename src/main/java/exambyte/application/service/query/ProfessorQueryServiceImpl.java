package exambyte.application.service.query;

import exambyte.application.dto.ProfessorDTO;
import exambyte.application.mapper.ProfessorDTOMapper;
import exambyte.domain.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorQueryServiceImpl implements ProfessorQueryService {

    private final ProfessorService service;
    private final ProfessorDTOMapper mapper;

    public ProfessorQueryServiceImpl(ProfessorService service, ProfessorDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Optional<UUID> getProfIdByName(String name) {
        return service.getProfessorIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessorById(UUID id) {
        return mapper.toDTO(service.getProfessor(id));
    }
}
