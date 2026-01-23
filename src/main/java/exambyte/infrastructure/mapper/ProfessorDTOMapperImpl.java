package exambyte.infrastructure.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.mapper.ProfessorDTOMapper;
import org.springframework.stereotype.Component;

@Component
public class ProfessorDTOMapperImpl implements ProfessorDTOMapper {

    @Override
    public ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(
                professor.uuid(),
                professor.getName());
    }
}
