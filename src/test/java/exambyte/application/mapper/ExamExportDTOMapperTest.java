package exambyte.application.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.domain.export_mapper.ExamExportDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExamExportDTOMapperTest {

    private ExamExportDTOMapper mapper;

    private ExamDTO exam;
    private QuestionDTO question1;
    private QuestionDTO question2;
    private CorrectAnswersDTO correctAnswers;

    @BeforeEach
    void setUp() {
        mapper = new ExamExportDTOMapperImpl();

        exam = new ExamDTO(
                UUID.randomUUID(),
                "Title",
                UUID.randomUUID(),
                null,
                null,
                null
        );

        question1 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 1",
                5,
                exam.id(),
                QuestionTypeDTO.FREE_RESPONSE
        );

        question2 = new QuestionDTO(
                UUID.randomUUID(),
                "Question 2",
                4,
                exam.id(),
                QuestionTypeDTO.MC
        );

        correctAnswers = new CorrectAnswersDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                question2.id()
        );
    }

    @Test
    void mapDTOToExport() {
        List<ExamExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Professor",
                9,
                List.of(question1, question2),
                List.of(correctAnswers));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();
        assertThat(firstElement.getExamTitle()).isEqualTo("Title");
        assertThat(firstElement.getTotalPoints()).isEqualTo(9);
        assertThat(firstElement.getAuthor()).isEqualTo("Professor");

        assertThat(firstElement.getQuestionText()).isEqualTo("Question 1");
        assertThat(firstElement.getQuestionType()).isEqualTo(question1.type().name());
        assertThat(firstElement.getQuestionPoints()).isEqualTo(5);

        assertThat(firstElement.getSolution()).isEmpty();
        assertThat(firstElement.getChoices()).isEmpty();

        var secondElement = result.getLast();

        assertThat(secondElement.getChoices()).isEqualTo("A\nB\nC\nD");
        assertThat(secondElement.getSolution()).isEqualTo("A\nB");
    }
}
