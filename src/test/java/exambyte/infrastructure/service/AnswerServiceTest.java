package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.domain.service.AnswerService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTest {

    private final AnswerRepository repository = mock(AnswerRepository.class);
    private final AnswerService service = new AnswerServiceImpl(repository);

    @Test
    void findByStudentIdAndQuestionId_answer_notFound() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID frageId = UUID.randomUUID();

        when(repository.findByStudentIdAndQuestionId(studentId, frageId)).thenReturn(Optional.empty());

        // Act
        Answer result = service.findByStudentIdAndQuestionId(studentId, frageId);

        // Assert
        assertNull(result);
        verify(repository).findByStudentIdAndQuestionId(studentId, frageId);
    }
}