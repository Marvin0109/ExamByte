package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.AnswerParser;
import exambyte.domain.service.McScoringPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AutomaticReviewServiceTest {

    private AutomaticReviewService automaticReviewService;
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final LocalDateTime SUBMIT_TIME =
            LocalDateTime.of(2020, 1, 1, 0, 0);

    @Mock
    private McScoringPolicy mcScoringPolicy;

    @Mock
    private AnswerParser answerParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        automaticReviewService = new AutomaticReviewServiceImpl(mcScoringPolicy, answerParser);
    }

    @Test
    void autoReviewMC() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                4.0,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "A\nB",
                question.id(),
                STUDENT_ID,
                SUBMIT_TIME
        );

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB\nC\nD",
                "A\nB\nC\nD\nE\nF\nG\nH",
                question.id()
        );

        when(answerParser.parseAnswer(correctAnswers.solution())).thenReturn(List.of("A", "B", "C", "D"));
        when(answerParser.parseAnswer(answer.answer())).thenReturn(List.of("A", "B"));

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(question),
                List.of(answer),
                List.of(correctAnswers),
                STUDENT_ID
        );

        ReviewDTO review = reviews.getFirst();

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(review.text()).startsWith("Lösung");
    }

    @ParameterizedTest()
    @MethodSource("scTestCases")
    void autoReviewSC(
            double questionPoints,
            String studentAnswer,
            String solution,
            double expectedPoints
    ) {
        // Arrange
        QuestionDTO frage = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                questionPoints,
                UUID.randomUUID(),
                QuestionTypeDTO.SC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                studentAnswer,
                frage.id(),
                STUDENT_ID,
                SUBMIT_TIME
        );

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                solution,
                "A\nB\nC\nD",
                frage.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewSC(
                List.of(frage),
                List.of(answer),
                List.of(correctAnswers),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().points()).isEqualTo(expectedPoints);
    }

    static Stream<Arguments> scTestCases() {
        return Stream.of(
                Arguments.of(1, "A", "A", 1),
                Arguments.of(0.5, "A", "A", 0.5),
                Arguments.of(1, "B", "A", 0)
        );
    }

    @Test
    void autoReviewSC_studentAnswerNotFound() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "Answer 2\nAnswer 4\nAnswer 5",
                "Answer 1\nAnswer 2\nAnswer 3\nAnswer 4\nAnswer 5",
                question.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewSC(
                List.of(question),
                List.of(),
                List.of(correctAnswers),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    void autoReviewSC_correctAnswersNotFound() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer 2\nAnswer 3\nAnswer 4",
                question.id(),
                STUDENT_ID,
                SUBMIT_TIME);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewSC(
                List.of(question),
                List.of(answer),
                List.of(),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    void autoReviewMC_studentAnswerNotFound() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        CorrectAnswersDTO correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "Answer 2\nAnswer 4\nAnswer 5",
                "Answer 1\nAnswer 2\nAnswer 3\nAnswer 4\nAnswer 5",
                question.id());

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(question),
                List.of(),
                List.of(correctAnswers),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).isEmpty();
    }

    @Test
    void autoReviewMC_correctAnswersNotFound() {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                3,
                UUID.randomUUID(),
                QuestionTypeDTO.MC);

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                "Answer 2\nAnswer 3\nAnswer 4",
                question.id(),
                STUDENT_ID,
                SUBMIT_TIME);

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(question),
                List.of(answer),
                List.of(),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).isEmpty();
    }
}
