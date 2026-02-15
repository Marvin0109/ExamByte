package exambyte.web.service.export_mapper;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.web.service.csv_dto.ReviewExportDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewExportDTOMapperImpl implements ReviewExportDTOMapper {

    @Override
    public ReviewExportDTO mapDTOToExport(ExamDTO exam,
                                          List<FrageDTO> fragen,
                                          List<AntwortDTO> antworten,
                                          List<ReviewDTO> reviews) {

    }
}
