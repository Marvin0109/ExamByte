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
    @DisplayName("Ein Exam wurde nicht gefunden")
    void test_01() {
        UUID examId = UUID.randomUUID();
        when(examRepository.findByFachId(any())).thenReturn(Optional.empty());

        assertThrows(NichtVorhandenException.class, () -> service.getExam(examId));
        verify(examRepository).findByFachId(examId);
    }
}
