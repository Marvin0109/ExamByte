package exambyte.infrastructure.persistence.repository;

import exambyte.domain.entitymapper.AnswerMapper;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.infrastructure.persistence.entities.AnswerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnswerRepositoryTest {

    private final AnswerDAO answerDAO = mock(AnswerDAO.class);
    private final AnswerMapper answerMapper = mock(AnswerMapper.class);

    private AnswerRepository repository;

    private static final UUID ANSWER_ID = UUID.randomUUID();
    private static final UUID FRAGE_ID = UUID.randomUUID();
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final LocalDateTime TIMESTAMP =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new AnswerRepositoryImpl(answerDAO, answerMapper);
    }

    @Test
    void findByQuestionId_exists() {
        // Arrange
        AnswerEntity entity = new AnswerEntity.AnswerEntityBuilder()
                .answer("Answer")
                .questionId(FRAGE_ID)
                .studentId(STUDENT_ID)
                .submitTime(TIMESTAMP)
                .build();
        Answer domain = new Answer.AnswerBuilder()
                .answer("Answer")
                .frageId(FRAGE_ID)
                .studentId(STUDENT_ID)
                .submitTime(TIMESTAMP)
                .build();

        when(answerDAO.findByQuestionId(ANSWER_ID)).thenReturn(Optional.of(entity));
        when(answerMapper.toDomain(entity)).thenReturn(domain);

        // Act
        Answer result = repository.findByQuestionId(ANSWER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    void findByQuestionId_notExists_returnsNull() {
        // Arrange
        when(answerDAO.findByQuestionId(ANSWER_ID)).thenReturn(Optional.empty());

        // Act
        Answer result = repository.findByQuestionId(ANSWER_ID);

        // Assert
        assertNull(result);
    }
}
