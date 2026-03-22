package exambyte.domain.export_mapper;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;

import java.util.List;

public interface ReviewExportDTOMapper {

    List<ReviewExportDTO> mapDTOToExport(ExamDTO exam,
                                   String reviewerName,
                                   double points,
                                   List<QuestionDTO> questions,
                                   List<AnswerDTO> answers,
                                   List<ReviewDTO> reviews);
}
