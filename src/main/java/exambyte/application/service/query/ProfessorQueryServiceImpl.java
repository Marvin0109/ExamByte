package exambyte.application.service.query;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.mapper.ProfessorDTOMapper;
import exambyte.domain.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfessorQueryServiceImpl implements ProfessorQueryService {

    private final ProfessorService professorService;
    private final ProfessorDTOMapper professorDTOMapper;

    public ProfessorQueryServiceImpl(ProfessorService professorService, ProfessorDTOMapper professorDTOMapper) {
        this.professorService = professorService;
        this.professorDTOMapper = professorDTOMapper;
    }

    @Override
    public Optional<UUID> getProfIdByName(String name) {
        return professorService.getProfessorIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessorById(UUID id) {
        return professorDTOMapper.toDTO(professorService.getProfessor(id));
    }
}
