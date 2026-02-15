package exambyte.web.service.export_mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.web.service.csv_dto.ReviewExportDTO;

import java.util.List;

public interface ReviewExportDTOMapper {

    ReviewExportDTO mapDTOToExport(ExamDTO exam,
                                   List<FrageDTO> fragen,
                                   List<AntwortDTO> antworten,
                                   List<ReviewDTO> reviews);
}
