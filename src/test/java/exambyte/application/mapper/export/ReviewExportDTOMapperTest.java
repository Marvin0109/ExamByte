package exambyte.application.mapper.export;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.dto.export.ReviewExportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewExportDTOMapperTest {

    private ReviewExportDTOMapper mapper;

    private ExamDTO exam;
    private QuestionDTO question1;
    private QuestionDTO question2;
    private AnswerDTO answer1;
    private AnswerDTO answer2;
    private ReviewDTO review1;
    private ReviewDTO review2;

    @BeforeEach
    void setUp() {
        mapper = new ReviewExportDTOMapperImpl();

        exam = new ExamDTO(
                UUID.randomUUID(),
                "Title",
                null,
                null,
                null,
                null
        );

        question1 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                6,
                exam.id(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        question2 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 2",
                1,
                exam.id(),
                QuestionTypeDTO.SC
        );

        answer1 = new AnswerDTO(
                UUID.randomUUID(),
                "Answer 1",
                question1.id(),
                null,
                null
        );

        answer2 = new AnswerDTO(
                UUID.randomUUID(),
                "A",
                question2.id(),
                null,
                null
        );

        review1 = new ReviewDTO(
                UUID.randomUUID(),
                answer1.id(),
                null,
                "Text 1",
                6
        );

        review2 = new ReviewDTO(
                UUID.randomUUID(),
                answer2.id(),
                null,
                "Text 2",
                1
        );
    }

    @Test
    void mapDTOToExport() {
        List<ReviewExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Reviewer",
                7,
                List.of(question1, question2),
                List.of(answer1, answer2),
                List.of(review1, review2));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();

        assertThat(firstElement.getExamTitle()).isEqualTo("Title");
        assertThat(firstElement.getAuthor()).isEqualTo("Reviewer");
        assertThat(firstElement.getTotalPoints()).isEqualTo(7);

        assertThat(firstElement.getQuestionText()).isEqualTo("Question 1");
        assertThat(firstElement.getQuestionType()).isEqualTo(question1.type().name());
        assertThat(firstElement.getQuestionPoints()).isEqualTo(6);

        assertThat(firstElement.getStudentAnswer()).isEqualTo("Answer 1");

        assertThat(firstElement.getReviewText()).isEqualTo("Text 1");
        assertThat(firstElement.getReviewPoints()).isEqualTo(6);

        var secondElement = result.getLast();

        assertThat(secondElement).isNotNull();
    }

    @Test
    void mapDTOToExport_nullReview() {
        List<ReviewExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Reviewer",
                7,
                List.of(question1, question2),
                List.of(answer1, answer2),
                List.of(review2));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();

        assertThat(firstElement.getReviewText()).isNull();
        assertThat(firstElement.getReviewPoints()).isEqualTo(0.0);

        var secondElement = result.getLast();

        assertThat(secondElement).isNotNull();
        assertThat(secondElement.getReviewText()).isNotEmpty();
    }
}
