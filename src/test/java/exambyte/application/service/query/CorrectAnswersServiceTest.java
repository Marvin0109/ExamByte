package exambyte.application.service.query;

import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.mapper.CorrectAnswersDTOMapper;
import exambyte.domain.model.exam.CorrectAnswers;
import exambyte.domain.repository.CorrectAnswersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CorrectAnswersServiceTest {

    private CorrectAnswersService queryService;

    @Mock
    private CorrectAnswersRepository repository;

    @Mock
    private CorrectAnswersDTOMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryService = new CorrectAnswersServiceImpl(repository, mapper);
    }

    @Test
    void getCorrectAnswerForQuestion_success() {
        UUID correctAnswerId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        CorrectAnswers correctAnswers = new CorrectAnswers.CorrectAnswersBuilder()
                .id(correctAnswerId)
                .questionId(questionId)
                .choices("A\nB")
                .solution("A")
                .build();

        when(repository.findByQuestionId(questionId)).thenReturn(Optional.of(correctAnswers));

        CorrectAnswersDTO correctAnswersDTO = new CorrectAnswersDTO(
                correctAnswerId,
                "A",
                "A\nB",
                questionId
        );

        when(mapper.toDTO(correctAnswers)).thenReturn(correctAnswersDTO);

        CorrectAnswersDTO result = queryService.getCorrectAnswerForQuestion(questionId);

        assertThat(result).isNotNull();
    }

    @Test
    void getCorrectAnswerForQuestion_fail() {
        when(repository.findByQuestionId(any())).thenReturn(Optional.empty());
        CorrectAnswersDTO result = queryService.getCorrectAnswerForQuestion(UUID.randomUUID());
        assertThat(result).isNull();
    }
}
