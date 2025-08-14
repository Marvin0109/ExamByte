package exambyte.infrastructure.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.model.common.QuestionType;
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
                QuestionTypeDTO.valueOf(frage.getType().name()),
                frage.getProfessorUUID(),
                frage.getExamUUID());
    }

    @Override
    public List<FrageDTO> toFrageDTOList(List<Frage> fragen) {
        return fragen.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Frage toDomain(FrageDTO dto) {
        return new Frage.FrageBuilder()
                .id(null)
                .fachId(dto.getFachId())
                .frageText(dto.getFrageText())
                .maxPunkte(dto.getMaxPunkte())
                .type(QuestionType.valueOf(dto.getType().name()))
                .professorUUID(dto.getProfessorUUID())
                .examUUID(dto.getExamUUID())
                .build();
    }
}
