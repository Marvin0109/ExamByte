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
                frage.getId(),
                frage.getFrageText(),
                frage.getMaxPunkte(),
                frage.getExamId(),
                QuestionTypeDTO.valueOf(frage.getType().name()));
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
                .id(dto.id())
                .frageText(dto.frageText())
                .maxPunkte(dto.maxPunkte())
                .type(QuestionType.valueOf(dto.type().name()))
                .examId(dto.examId())
                .build();
    }
}
