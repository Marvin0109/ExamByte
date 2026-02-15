package exambyte.domain.export_mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;

import java.util.List;

public interface ReviewExportDTOMapper {

    List<ReviewExportDTO> mapDTOToExport(ExamDTO exam,
                                   String reviewerName,
                                   int maxPunkte,
                                   List<FrageDTO> fragen,
                                   List<AntwortDTO> antworten,
                                   List<ReviewDTO> reviews);
}
