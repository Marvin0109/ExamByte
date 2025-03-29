package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.ExamService;
import exambyte.infrastructure.NichtVorhandenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExamServiceTest {

    private final ExamRepository examRepository = mock(ExamRepository.class);
    private final ExamService service = new ExamServiceImpl(examRepository);

    @Test
    @DisplayName("Gebe alle Exams als Liste zurück")
    void test_01() {
        // Arrange
        Exam exam1 = new Exam.ExamBuilder().build();
        Exam exam2 = new Exam.ExamBuilder().build();

        List<Exam> exams = List.of(exam1, exam2);
        when(examRepository.findAll()).thenReturn(exams);

        // Act
        List<Exam> list = service.alleExams();

        // Assert
        assertThat(list).hasSize(2);
        verify(examRepository).findAll();
    }

    @Test
    @DisplayName("Suche Exam nach UUID")
    void test_02() {
        // Arrange
        var fachId = UUID.randomUUID();
        Exam exam1 = new Exam.ExamBuilder().fachId(fachId).build();

        when(examRepository.findByFachId(fachId)).thenReturn(Optional.of(exam1));

        // Act
        var result = service.getExam(fachId);

        assertThat(result).isNotNull();
        assertThat(result.getFachId()).isEqualTo(fachId);
        verify(examRepository).findByFachId(fachId);
    }

    @Test
    @DisplayName("Ein Exam wurde nicht gefunden")
    void test_03() {
        UUID examId = UUID.randomUUID();
        when(examRepository.findByFachId(any())).thenReturn(Optional.empty());
        assertThrows(NichtVorhandenException.class, () -> service.getExam(examId));
        verify(examRepository).findByFachId(examId);
    }

    @Test
    @DisplayName("Ein Exam kann gespeichert werden")
    void test_04() {
        // Arrange
        UUID fachId = UUID.randomUUID();
        String title = "Titel";
        UUID profFachId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 1, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 2, 1, 1, 0);

        // Act
        service.addExam(null, fachId, title, profFachId, startTime, endTime, resultTime);

        // Assert
        verify(examRepository).save(any(Exam.class));
    }
}
