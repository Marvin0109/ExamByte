package exambyte.web.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.application.service.CsvExportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvExportServiceImpl implements CsvExportService {

    @Override
    public byte[] exportExamToCsv(List<ExamExportDTO> examExportDTO) throws Exception{
        CsvMapper csvMapper = new CsvMapper();

        CsvSchema schema = csvMapper
                .schemaFor(ExamExportDTO.class)
                .withHeader()
                .withColumnSeparator(';');

        return csvMapper.writer(schema)
                .writeValueAsBytes(examExportDTO);
    }

    @Override
    public byte[] exportReviewToCsv(List<ReviewExportDTO> reviewExportDTO) throws Exception {
        CsvMapper csvMapper = new CsvMapper();

        CsvSchema schema = csvMapper
                .schemaFor(ReviewExportDTO.class)
                .withHeader()
                .withColumnSeparator(';');

        return csvMapper.writer(schema)
                .writeValueAsBytes(reviewExportDTO);
    }
}
