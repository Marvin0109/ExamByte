package exambyte.service.mapper;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.service.dto.AntwortDTO;

public class AntwortMapperDTO {

    public static AntwortDTO toDTO(Antwort antwort) {
        return new AntwortDTO(null, antwort.getFachId(), antwort.getAntwortText(), antwort.getFrageFachId(),
                antwort.getStudentUUID(), antwort.getAntwortZeitpunkt(), antwort.getLastChangesZeitpunkt());
    }

    public static Antwort toAntwort(AntwortDTO dto) {
        return new Antwort.AntwortBuilder()
                .id(null)
                .fachId(dto.getFachId())
                .antwortText(dto.getAntwortText())
                .frageFachId(dto.getFrageFachId())
                .studentFachId(dto.getStudentFachId())
                .antwortZeitpunkt(dto.getAntwortZeitpunkt())
                .lastChangesZeitpunkt(dto.getLastChangesZeitpunkt())
                .build();
    }
}
