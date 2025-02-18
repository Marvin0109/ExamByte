package exambyte.infrastructure.mapper;

import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.mapper.FrageDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FrageDTOMapperImpl implements FrageDTOMapper {

    @Override
    public FrageDTO toDTO(Frage frage) {
        return new FrageDTO(
                null,
                frage.getFachId(),
                frage.getFrageText(),
                frage.getMaxPunkte(),
                frage.getProfessorUUID(),
                frage.getExamUUID());
    }

    @Override
    public List<FrageDTO> toFrageDTOList(List<Frage> fragen) {
        return fragen.stream()
                .map(this::toDTO)
                .toList();
    }
}
