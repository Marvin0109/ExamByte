package exambyte.web.service.export_mapper;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.web.service.csv_dto.ExamExportDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamExportDTOMapperImpl implements ExamExportDTOMapper {

    @Override
    public ExamExportDTO mapDTOToExport(ExamDTO exam, List<FrageDTO> fragen, List<KorrekteAntwortenDTO> loesungen) {

    }
}
