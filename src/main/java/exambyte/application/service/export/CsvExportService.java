package exambyte.application.service.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.dto.export.ReviewExportDTO;
import exambyte.application.exception.CsvExportException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvExportService {

    private final CsvMapper csvMapper = new CsvMapper();

    public byte[] exportExamToCsv(List<ExamExportDTO> examExportDTO) {
        try {
            CsvSchema schema = csvMapper
                    .schemaFor(ExamExportDTO.class)
                    .withHeader()
                    .withColumnSeparator(';');

            return csvMapper.writer(schema)
                    .writeValueAsBytes(examExportDTO);
        } catch (JsonProcessingException e) {
            throw new CsvExportException("CSV export failed for exam", e);
        }
    }

    public byte[] exportReviewToCsv(List<ReviewExportDTO> reviewExportDTO)  {
        try {
            CsvSchema schema = csvMapper
                    .schemaFor(ReviewExportDTO.class)
                    .withHeader()
                    .withColumnSeparator(';');

            return csvMapper.writer(schema)
                    .writeValueAsBytes(reviewExportDTO);
        } catch (JsonProcessingException e) {
            throw new CsvExportException("CSV export failed for review", e);
        }
    }
}
