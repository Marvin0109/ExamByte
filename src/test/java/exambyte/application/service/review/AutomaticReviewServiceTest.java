package exambyte.application.service.review;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AutomaticReviewServiceTest {

    private AutomaticReviewService automaticReviewService;
    private static final UUID STUDENT_ID = UUID.randomUUID();
    private static final LocalDateTime SUBMIT_TIME = LocalDateTime.of(2020, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        automaticReviewService = new AutomaticReviewServiceImpl();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("mcTestCases")
    void autoReviewMC(
            String reviewText,
            double questionPoints,
            String studentAnswer,
            String solution,
            double expectedPoints
    ) {
        // Arrange
        QuestionDTO question = new QuestionDTO(
                UUID.randomUUID(),
                "Question",
                questionPoints,
                UUID.randomUUID(),
                QuestionTypeDTO.MC
        );

        AnswerDTO answer = new AnswerDTO(
                UUID.randomUUID(),
                studentAnswer,
                question.id(),
                STUDENT_ID,
                SUBMIT_TIME
        );

        CorrectAnswersDTO correctAnswersDTO = new CorrectAnswersDTO(
                UUID.randomUUID(),
                solution,
                "A\nB\nC\nD\nE\nF\nG\nH",
                question.id()
        );

        // Act
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
                List.of(question),
                List.of(answer),
                List.of(correctAnswersDTO),
                STUDENT_ID
        );

        // Assert
        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().points()).isEqualTo(expectedPoints);
    }

    static Stream<Arguments> mcTestCases() {
        return Stream.of(
                Arguments.of("Everything correct", 4.0, "A\nB\nC\nD", "A\nB\nC\nD", 4.0),
                Arguments.of("3 correct, 0 wrong", 4.0, "A\nB\nC", "A\nB\nC\nD", 3.0),
                Arguments.of("3 correct, 1 wrong", 4.0, "A\nB\nC\nE", "A\nB\nC\nD", 2.0),
                Arguments.of("2 correct, 0 wrong", 4.0, "A\nB", "A\nB\nC\nD", 2.0),
                Arguments.of("2 correct, 1 wrong", 4.0, "A\nB\nE", "A\nB\nC\nD", 1.0),
                Arguments.of("2 correct, 2 wrong", 4.0, "A\nB\nE\nF", "A\nB\nC\nD", 0.0),
                Arguments.of("1 correct, 0 wrong", 4.0, "A", "A\nB\nC\nD", 1.0),
                Arguments.of("1 correct, 1 wrong", 4.0, "A\nE", "A\nB\nC\nD", 0.0),
                Arguments.of("0 correct, 0 wrong", 4.0, "", "A\nB\nC\nD", 0.0),
                Arguments.of("Everything wrong", 4.0, "E\nF\nG\nH", "A\nB\nC\nD", 0.0),

                Arguments.of("Everything correct", 3.5, "A\nB\nC\nD", "A\nB\nC\nD", 3.5),
                Arguments.of("3 correct, 0 wrong", 3.5, "A\nB\nC\nD", "A\nB\nC", 2.5),
                Arguments.of("3 correct, 1 wrong", 3.5, "A\nB\nC\nD", "A\nB\nC\nE", 2.0),
                Arguments.of("2 correct, 1 wrong", 3.5, "A\nB\nC\nD", "A\nB\nE", 0.0),
                Arguments.of("1 correct, 1 wrong", 3.5, "A\nB\nC\nD", "A\nE", 0.0),
                Arguments.of("Everything wrong", 3.5, "A\nB\nC\nD", "E\nF\nG\nH", 0.0),

                Arguments.of("3 correct, 1 wrong", 2.0, "A\nB\nC\nE", "A\nB\nC\nD", 1.0),
                Arguments.of("2 correct, 1 wrong", 2.0, "A\nB\nE", "A\nB\nC\nD", 0.5)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("scTestCases")
    void autoReviewSC(
            String reviewText,
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
        List<ReviewDTO> reviews = automaticReviewService.autoReviewMC(
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
                Arguments.of("Correct", 1, "A", "A", 1),
                Arguments.of("Correct", 0.5, "A", "A", 0.5),
                Arguments.of("Wrong", 1, "B", "A", 0)
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
