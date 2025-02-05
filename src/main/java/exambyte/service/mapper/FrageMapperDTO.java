package exambyte.service.mapper;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.service.dto.FrageDTO;

public class FrageMapperDTO {

    public static FrageDTO toDTO(Frage frage) {
        return new FrageDTO(null, frage.getFachId(), frage.getFrageText(), frage.getMaxPunkte(),
                frage.getProfessorUUID(), frage.getExamUUID());
    }

    public static Frage toDomain(FrageDTO dto) {
        return new Frage.FrageBuilder()
                .id(null)
                .fachId(dto.getFachId())
                .frageText(dto.getFrageText())
                .maxPunkte(dto.getMaxPunkte())
                .professorUUID(dto.getProfessorUUID())
                .examUUID(dto.getExamUUID())
                .build();
    }
}
