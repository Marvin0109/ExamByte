package exambyte.infrastructure.mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.mapper.AntwortDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AntwortDTOMapperImpl implements AntwortDTOMapper {

    @Override
    public AntwortDTO toDTO(Antwort antwort) {
        return new AntwortDTO(antwort.getId(),
                antwort.getAntwortText(),
                antwort.getFrageId(),
                antwort.getStudentUUID(),
                antwort.getAntwortZeitpunkt());
    }

    @Override
    public List<AntwortDTO> toAntwortDTOList(List<Antwort> antworten) {
        return antworten.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Antwort toDomain(AntwortDTO dto) {
        return new Antwort.AntwortBuilder()
                .id(dto.id())
                .antwortText(dto.antwortText())
                .frageId(dto.frageId())
                .studentId(dto.studentId())
                .antwortZeitpunkt(dto.antwortZeitpunkt())
                .build();
    }
}
