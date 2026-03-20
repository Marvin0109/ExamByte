package exambyte.application.mapper;

import exambyte.application.dto.AnswerDTO;
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
                                          double maxPunkte,
                                          List<FrageDTO> fragen,
                                          List<AnswerDTO> answers,
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

            AnswerDTO answer = answers.stream()
                    .filter(a -> a.frageId().equals(frage.id()))
                    .findAny()
                    .orElse(null);

            r.setStudiAntworten(answer == null ? "" : answer.answer());

            if (answer != null) {
                ReviewDTO review = reviews.stream()
                        .filter(reviewDTO -> reviewDTO.answerId().equals(answer.id()))
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
