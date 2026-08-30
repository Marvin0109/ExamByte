package exambyte.application.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.user.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorDTOMapper {

    public ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(
                professor.id(),
                professor.getName());
    }
}
