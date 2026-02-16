package exambyte.application.mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.domain.export_mapper.ReviewExportDTOMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewExportDTOMapperImpl implements ReviewExportDTOMapper {

    @Override
    public List<ReviewExportDTO> mapDTOToExport(ExamDTO exam,
                                          String reviewerName,
                                          int maxPunkte,
                                          List<FrageDTO> fragen,
                                          List<AntwortDTO> antworten,
                                          List<ReviewDTO> reviews) {

        List<ReviewExportDTO> export = new ArrayList<>();

        for(FrageDTO frage : fragen) {
            ReviewExportDTO r = new ReviewExportDTO();
            r.setExamTitle(exam.title());
            r.setAuthor(reviewerName);
            r.setMaxPunkte(maxPunkte);

            r.setFrageText(frage.frageText());
            r.setFrageTyp(frage.type().name());
            r.setPunkte(frage.maxPunkte());

            AntwortDTO a = antworten.stream()
                    .filter(antwort -> antwort.frageFachId().equals(frage.fachId()))
                    .findAny()
                    .orElse(null);

            r.setStudiAntworten(a == null ? "" : a.antwortText());

            if (a != null) {
                ReviewDTO review = reviews.stream()
                        .filter(reviewDTO -> reviewDTO.antwortFachId().equals(a.fachId()))
                        .findAny()
                        .orElse(null);

                if (review != null) {
                    r.setBewertung(review.bewertung());
                    r.setErreichtePunkte(review.punkte());
                }
            }

            export.add(r);
        }

        return export;
    }
}
