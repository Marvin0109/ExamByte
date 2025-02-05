package exambyte.service.mapper;

import exambyte.domain.aggregate.user.Professor;
import exambyte.service.dto.ProfessorDTO;

public class ProfessorMapperDTO {

    public static ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(null, professor.uuid(), professor.getName());
    }

    public static Professor toDomain(ProfessorDTO dto) {
        return new Professor.ProfessorBuilder()
                .id(null)
                .fachId(dto.fachId())
                .name(dto.name())
                .build();
    }
}
