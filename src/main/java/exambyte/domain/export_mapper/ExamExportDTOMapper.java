package exambyte.domain.export_mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;

import java.util.List;

public interface ExamExportDTOMapper {

    List<ExamExportDTO> mapDTOToExport(ExamDTO examDTO,
                                 String profName,
                                 double punkte,
                                 List<FrageDTO> fragen,
                                 List<CorrectAnswersDTO> correctAnswers);
}
