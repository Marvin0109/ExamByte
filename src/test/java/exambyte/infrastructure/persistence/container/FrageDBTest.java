package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.entitymapper.ExamMapper;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import exambyte.infrastructure.persistence.mapper.ExamMapperImpl;
import exambyte.infrastructure.persistence.mapper.FrageMapperImpl;
import exambyte.infrastructure.persistence.mapper.ProfessorMapperImpl;
import exambyte.infrastructure.persistence.repository.*;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.repository.FrageRepository;
import exambyte.domain.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class FrageDBTest {

    @Autowired
    private FrageDAO frageRepository;

    @Autowired
    private ProfessorDAO professorRepository;

    @Autowired
    private ExamDAO examRepository;

    private FrageRepository repository;
    private ProfessorRepository repository2;
    private ExamRepository repository3;

    @BeforeEach
    void setUp() {
        FrageMapper frageMapper = new FrageMapperImpl();
        ProfessorMapper professorMapper = new ProfessorMapperImpl();
        ExamMapper examMapper = new ExamMapperImpl();

        repository = new FrageRepositoryImpl(professorRepository, frageRepository, frageMapper);
        repository2 = new ProfessorRepositoryImpl(professorRepository, professorMapper);
        repository3 = new ExamRepositoryImpl(examRepository, examMapper);
    }

    @Test
    @DisplayName("Eine Frage kann gespeichert und wieder geladen werden, außerdem kann der Professor extrahiert werden")
    void test_01() {
        // Arrange
        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. Lowkey")
                .build();

        repository2.save(professor);

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);
        Exam exam = new Exam.ExamBuilder()
                .id(null)
                .fachId(null)
                .title("Test 1")
                .professorFachId(professor.uuid())
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        repository3.save(exam);

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(null)
                .frageText("Was ist Java?")
                .maxPunkte(6)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        // Act
        repository.save(frage);
        Optional<FrageEntity> geladen = frageRepository.findByFachId(frage.getFachId());
        ProfessorEntity extraction = ((FrageRepositoryImpl) repository).findByProfFachId(professor.uuid());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getFrageText()).isEqualTo("Was ist Java?");
        assertThat(geladen.get().getMaxPunkte()).isEqualTo(6);
        assertThat(geladen.get().getFachId()).isEqualTo(frage.getFachId());

        assertThat(extraction.getFachId()).isEqualTo(professor.uuid());
        assertThat(extraction.getName()).isEqualTo("Dr. Lowkey");

        assertThat(exam.getFachId()).isEqualTo(frage.getExamUUID());
    }

    // TODO
    @Test
    @DisplayName("Teste deleteAll")
    void test_02() {

    }
}
