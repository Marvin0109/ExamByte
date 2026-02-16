package exambyte.application.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.domain.export_mapper.ReviewExportDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewExportDTOMapperTest {

    private ReviewExportDTOMapper mapper;

    private ExamDTO exam;
    private FrageDTO frage1;
    private FrageDTO frage2;
    private AntwortDTO antwort1;
    private AntwortDTO antwort2;
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

        frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                6,
                null,
                exam.fachId(),
                QuestionTypeDTO.FREITEXT
        );

        frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                1,
                null,
                exam.fachId(),
                QuestionTypeDTO.SC
        );

        antwort1 = new AntwortDTO(
                UUID.randomUUID(),
                "Antwort 1",
                frage1.fachId(),
                null,
                null
        );

        antwort2 = new AntwortDTO(
                UUID.randomUUID(),
                "A",
                frage2.fachId(),
                null,
                null
        );

        review1 = new ReviewDTO(
                UUID.randomUUID(),
                antwort1.fachId(),
                null,
                "Bewertung 1",
                6
        );

        review2 = new ReviewDTO(
                UUID.randomUUID(),
                antwort2.fachId(),
                null,
                "Bewertung 2",
                1
        );
    }

    @Test
    void mapDTOToExport() {
        List<ReviewExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Reviewer",
                7,
                List.of(frage1, frage2),
                List.of(antwort1, antwort2),
                List.of(review1, review2));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();

        assertThat(firstElement.getExamTitle()).isEqualTo("Title");
        assertThat(firstElement.getAuthor()).isEqualTo("Reviewer");
        assertThat(firstElement.getMaxPunkte()).isEqualTo(7);

        assertThat(firstElement.getFrageText()).isEqualTo("Frage 1");
        assertThat(firstElement.getFrageTyp()).isEqualTo(frage1.type().name());
        assertThat(firstElement.getPunkte()).isEqualTo(6);

        assertThat(firstElement.getStudiAntworten()).isEqualTo("Antwort 1");

        assertThat(firstElement.getBewertung()).isEqualTo("Bewertung 1");
        assertThat(firstElement.getErreichtePunkte()).isEqualTo(6);

        var secondElement = result.getLast();

        assertThat(secondElement).isNotNull();
    }

    @Test
    void mapDTOToExport_nullReview() {
        List<ReviewExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Reviewer",
                7,
                List.of(frage1, frage2),
                List.of(antwort1, antwort2),
                List.of(review2));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();

        assertThat(firstElement.getBewertung()).isNull();
        assertThat(firstElement.getErreichtePunkte()).isEqualTo(0.0);

        var secondElement = result.getLast();

        assertThat(secondElement).isNotNull();
        assertThat(secondElement.getBewertung()).isNotEmpty();
    }
}
