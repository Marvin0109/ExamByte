package exambyte.infrastructure.repository;

import exambyte.infrastructure.mapper.AnswerMapper;
import exambyte.domain.model.exam.Answer;
import exambyte.domain.repository.AnswerRepository;
import exambyte.infrastructure.entity.AnswerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnswerRepositoryTest {

    private final AnswerDAO dao = mock(AnswerDAO.class);
    private final AnswerMapper mapper = mock(AnswerMapper.class);

    private AnswerRepository repository;

    private static final UUID ANSWER_ID = UUID.randomUUID();
    private static final UUID ANSWER_ID_2 = UUID.randomUUID();
    private static final UUID QUESTION_ID = UUID.randomUUID();
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final UUID STUDENT_ID_2 = UUID.randomUUID();
    private static final LocalDateTime SUBMIT_TIME =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        repository = new AnswerRepositoryImpl(dao, mapper);
    }

    @Test
    void findByQuestionId_exists() {
        // Arrange
        AnswerEntity entity1 = new AnswerEntity.AnswerEntityBuilder()
                .id(ANSWER_ID)
                .answer("Answer")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID)
                .submitTime(SUBMIT_TIME)
                .build();
        AnswerEntity entity2 = new AnswerEntity.AnswerEntityBuilder()
                .id(ANSWER_ID_2)
                .answer("Answer 2")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID_2)
                .submitTime(SUBMIT_TIME)
                .build();
        Answer domain1 = new Answer.AnswerBuilder()
                .answer("Answer")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID)
                .submitTime(SUBMIT_TIME)
                .build();
        Answer domain2 = new Answer.AnswerBuilder()
                .answer("Answer 2")
                .questionId(QUESTION_ID)
                .studentId(STUDENT_ID_2)
                .submitTime(SUBMIT_TIME).build();

        when(dao.findByQuestionId(QUESTION_ID)).thenReturn(List.of(entity1, entity2));
        when(mapper.toDomain(entity1)).thenReturn(domain1);
        when(mapper.toDomain(entity2)).thenReturn(domain2);

        // Act
        List<Answer> result = repository.findByQuestionId(QUESTION_ID);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst()).isEqualTo(domain1);
    }

    @Test
    void findByQuestionId_notExists_returnsNull() {
        // Arrange
        when(dao.findByQuestionId(QUESTION_ID)).thenReturn(List.of());

        // Act
        List<Answer> result = repository.findByQuestionId(QUESTION_ID);

        // Assert
        assertThat(result).isEmpty();
    }
}
