package exambyte.infrastructure.mapper;

import exambyte.application.dto.ProfessorDTO;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.mapper.ProfessorDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessorDTOMapperImpl implements ProfessorDTOMapper {

    @Override
    public ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(
                null,
                professor.uuid(),
                professor.getName());
    }

    @Override
    public List<ProfessorDTO> toProfessorDTOList(List<Professor> professoren) {
        return professoren.stream()
                .map(this::toDTO)
                .toList();
    }
}
