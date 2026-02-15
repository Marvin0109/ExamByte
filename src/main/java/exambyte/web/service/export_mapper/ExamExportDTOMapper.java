package exambyte.web.service.export_mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.web.service.csv_dto.ExamExportDTO;

import java.util.List;

public interface ExamExportDTOMapper {

    ExamExportDTO mapDTOToExport(ExamDTO examDTO, List<FrageDTO> fragen, List<KorrekteAntwortenDTO> loesungen);
}
