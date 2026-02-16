package exambyte.application.mapper;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
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
    private FrageDTO frage1;
    private FrageDTO frage2;
    private KorrekteAntwortenDTO korrekteAntworten;

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

        frage1 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 1",
                5,
                null,
                exam.fachId(),
                QuestionTypeDTO.FREITEXT
        );

        frage2 = new FrageDTO(
                UUID.randomUUID(),
                "Frage 2",
                4,
                null,
                exam.fachId(),
                QuestionTypeDTO.MC
        );

        korrekteAntworten = new KorrekteAntwortenDTO(
                UUID.randomUUID(),
                "A\nB",
                "A\nB\nC\nD",
                frage2.fachId()
        );
    }

    @Test
    void mapDTOToExport() {
        List<ExamExportDTO> result = mapper.mapDTOToExport(
                exam,
                "Professor",
                9,
                List.of(frage1, frage2),
                List.of(korrekteAntworten));

        assertThat(result).hasSize(2);

        var firstElement = result.getFirst();
        assertThat(firstElement.getExamTitle()).isEqualTo("Title");
        assertThat(firstElement.getMaxPunkte()).isEqualTo(9);
        assertThat(firstElement.getAuthor()).isEqualTo("Professor");

        assertThat(firstElement.getFrageText()).isEqualTo("Frage 1");
        assertThat(firstElement.getFrageTyp()).isEqualTo(frage1.type().name());
        assertThat(firstElement.getPunkte()).isEqualTo(5);

        assertThat(firstElement.getLoesungen()).isEmpty();
        assertThat(firstElement.getAntwortMoeglichkeiten()).isEmpty();

        var secondElement = result.getLast();

        assertThat(secondElement.getAntwortMoeglichkeiten()).isEqualTo("A\nB\nC\nD");
        assertThat(secondElement.getLoesungen()).isEqualTo("A\nB");
    }
}
