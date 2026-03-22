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

    private final AnswerDAO dao = mock(AnswerDAO.class);
    private final AnswerMapper mapper = mock(AnswerMapper.class);

    private AnswerRepository repository;

    private static final UUID ANSWER_ID = UUID.randomUUID();
    private static final UUID QUESTION_ID = UUID.randomUUID();
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final LocalDateTime SUBMIT_TIME =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new AnswerRepositoryImpl(dao, mapper);
    }

    @Test
    void findByQuestionId_exists() {
        // Arrange
        AnswerEntity entity = new AnswerEntity.AnswerEntityBuilder()
                .answer("Answer")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID)
                .submitTime(SUBMIT_TIME)
                .build();
        Answer domain = new Answer.AnswerBuilder()
                .answer("Answer")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID)
                .submitTime(SUBMIT_TIME)
                .build();

        when(dao.findByQuestionId(ANSWER_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        Answer result = repository.findByQuestionId(ANSWER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    void findByQuestionId_notExists_returnsNull() {
        // Arrange
        when(dao.findByQuestionId(ANSWER_ID)).thenReturn(Optional.empty());

        // Act
        Answer result = repository.findByQuestionId(ANSWER_ID);

        // Assert
        assertNull(result);
    }
}
