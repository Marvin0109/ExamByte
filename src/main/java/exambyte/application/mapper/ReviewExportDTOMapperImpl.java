package exambyte.application.mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
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
                                          double points,
                                          List<QuestionDTO> questions,
                                          List<AnswerDTO> answers,
                                          List<ReviewDTO> reviews) {

        List<ReviewExportDTO> export = new ArrayList<>();

        for(QuestionDTO frage : questions) {
            ReviewExportDTO r = new ReviewExportDTO();
            r.setExamTitle(exam.title());
            r.setAuthor(reviewerName);
            r.setTotalPoints(points);

            r.setQuestionText(frage.text());
            r.setQuestionType(frage.type().name());
            r.setQuestionPoints(frage.points());

            AnswerDTO answer = answers.stream()
                    .filter(a -> a.questionId().equals(frage.id()))
                    .findAny()
                    .orElse(null);

            r.setStudentAnswer(answer == null ? "" : answer.answer());

            if (answer != null) {
                ReviewDTO review = reviews.stream()
                        .filter(reviewDTO -> reviewDTO.answerId().equals(answer.id()))
                        .findAny()
                        .orElse(null);

                if (review != null) {
                    r.setReviewText(review.text());
                    r.setReviewPoints(review.points());
                }
            }

            export.add(r);
        }

        return export;
    }
}
