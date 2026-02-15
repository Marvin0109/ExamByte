package exambyte.application.mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;

import java.util.List;

public interface ExamExportDTOMapper {

    List<ExamExportDTO> mapDTOToExport(ExamDTO examDTO,
                                 String profName,
                                 int punkte,
                                 List<FrageDTO> fragen,
                                 List<KorrekteAntwortenDTO> loesungen);
}
