package exambyte.infrastructure.mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.mapper.AntwortDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AntwortDTOMapperImpl implements AntwortDTOMapper {

    @Override
    public AntwortDTO toDTO(Antwort antwort) {
        return new AntwortDTO.AntwortDTOBuilder()
                .id(null)
                .fachId(antwort.getFachId())
                .antwortText(antwort.getAntwortText())
                .frageFachId(antwort.getFrageFachId())
                .studentFachId(antwort.getStudentUUID())
                .antwortZeitpunkt(antwort.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(antwort.getLastChangesZeitpunkt())
                .build();
    }

    @Override
    public List<AntwortDTO> toAntwortDTOList(List<Antwort> antworten) {
        return antworten.stream()
                .map(this::toDTO)
                .toList();
    }
}
