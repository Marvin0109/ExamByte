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
                frage.getFachId(),
                frage.getFrageText(),
                frage.getMaxPunkte(),
                frage.getProfessorUUID(),
                frage.getExamUUID(),
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
                .fachId(dto.fachId())
                .frageText(dto.frageText())
                .maxPunkte(dto.maxPunkte())
                .type(QuestionType.valueOf(dto.type().name()))
                .professorUUID(dto.profUUID())
                .examUUID(dto.examUUID())
                .build();
    }
}
