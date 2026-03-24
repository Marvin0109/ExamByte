package exambyte.web.service;

import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.dto.export.ReviewExportDTO;
import exambyte.application.service.export.CsvExportService;
import exambyte.application.service.export.CsvExportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvExportServiceTest {

    private CsvExportService service;

    @BeforeEach
    void setUp() {
        service = new CsvExportServiceImpl();
    }

    @Test
    void exportExamToCsv() {
        ExamExportDTO dto = new ExamExportDTO();
        dto.setExamTitle("Test 1");
        dto.setTotalPoints(10);

        List<ExamExportDTO> list = List.of(dto);

        byte[] result = service.exportExamToCsv(list);

        String csv = new String(result, StandardCharsets.UTF_8);

        assertNotNull(result);
        assertTrue(csv.contains("Pruefungstitel"));
        assertTrue(csv.contains("Test 1"));
        assertTrue(csv.contains(";"));
    }

    @Test
    void exportExamToCsv_emptyList() {
        List<ExamExportDTO> list = List.of();

        byte[] result = service.exportExamToCsv(list);

        String csv = new String(result, StandardCharsets.UTF_8);

        assertNotNull(result);
        assertTrue(csv.contains("Pruefungstitel"));
    }

    @Test
    void exportReviewToCsv() {
        ReviewExportDTO dto = new ReviewExportDTO();
        dto.setExamTitle("Test 1");
        dto.setTotalPoints(10);

        List<ReviewExportDTO> list = List.of(dto);

        byte[] result = service.exportReviewToCsv(list);

        String csv = new String(result, StandardCharsets.UTF_8);

        assertNotNull(result);
        assertTrue(csv.contains("Pruefungstitel"));
        assertTrue(csv.contains("Test 1"));
        assertTrue(csv.contains(";"));
    }

    @Test
    void exportReviewToCsv_emptyList() {
        List<ReviewExportDTO> list = List.of();

        byte[] result = service.exportReviewToCsv(list);

        String csv = new String(result, StandardCharsets.UTF_8);

        assertNotNull(result);
        assertTrue(csv.contains("Pruefungstitel"));
    }
}
