package exambyte.infrastructure.service;

import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.ExamService;
import exambyte.application.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExamServiceTest {

    private final ExamRepository repository = mock(ExamRepository.class);
    private final ExamService service = new ExamServiceImpl(repository);

    @Test
    void getExam_examNotFound() {
        UUID examId = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getExam(examId));
        verify(repository).findById(examId);
    }
}
