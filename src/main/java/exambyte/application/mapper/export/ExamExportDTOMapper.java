package exambyte.application.mapper.export;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.export.ExamExportDTO;

import java.util.List;

public interface ExamExportDTOMapper {

    List<ExamExportDTO> mapDTOToExport(ExamDTO examDTO,
                                 String profName,
                                 double points,
                                 List<QuestionDTO> questions,
                                 List<CorrectAnswersDTO> correctAnswers);
}
