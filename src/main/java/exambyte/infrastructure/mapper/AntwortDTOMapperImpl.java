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
        return new AntwortDTO(antwort.getFachId(),
                antwort.getAntwortText(),
                antwort.getFrageFachId(),
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
                .fachId(dto.fachId())
                .antwortText(dto.antwortText())
                .frageFachId(dto.frageFachId())
                .studentFachId(dto.studentFachId())
                .antwortZeitpunkt(dto.antwortZeitpunkt())
                .build();
    }
}
