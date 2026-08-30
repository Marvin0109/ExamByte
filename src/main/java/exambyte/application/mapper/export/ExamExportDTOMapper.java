package exambyte.application.mapper.export;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.export.ExamExportDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamExportDTOMapper {

    public List<ExamExportDTO> mapDTOToExport(ExamDTO exam,
                                              String profName,
                                              double points,
                                              List<QuestionDTO> questions,
                                              List<CorrectAnswersDTO> correctAnswers) {

        List<ExamExportDTO> export = new ArrayList<>();

        for (QuestionDTO question : questions) {
            ExamExportDTO e = new ExamExportDTO();
            e.setExamTitle(exam.title());
            e.setAuthor(profName);
            e.setTotalPoints(points);

            e.setQuestionText(question.text());
            e.setQuestionType(question.type().name());
            e.setQuestionPoints(question.points());

            CorrectAnswersDTO k = correctAnswers.stream()
                    .filter(l -> l.questionId().equals(question.id()))
                    .findAny()
                    .orElse(null);

            e.setChoices(k == null ? "" : k.choices());
            e.setSolution(k == null ? "" : k.solution());

            export.add(e);
        }

        return export;
    }
}
