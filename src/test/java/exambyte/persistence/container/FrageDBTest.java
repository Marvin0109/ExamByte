package exambyte.persistence.container;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ExamEntity;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.ExamMapper;
import exambyte.persistence.mapper.FrageMapper;
import exambyte.persistence.mapper.ProfessorMapper;
import exambyte.persistence.repository.*;
import exambyte.persistence.repository.impl.ExamRepositoryImpl;
import exambyte.persistence.repository.impl.FrageRepositoryImpl;
import exambyte.persistence.repository.impl.ProfessorRepositoryImpl;
import exambyte.service.repository.api.ExamRepository;
import exambyte.service.repository.api.FrageRepository;
import exambyte.service.repository.api.ProfessorRepository;
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
    private SpringDataFrageRepository frageRepository;

    @Autowired
    private SpringDataProfessorRepository professorRepository;

    @Autowired
    private SpringDataExamRepository examRepository;

    private FrageRepository repository;
    private ProfessorRepository repository2;
    private ExamRepository repository3;

    @BeforeEach
    void setUp() {
        repository = new FrageRepositoryImpl(professorRepository, frageRepository, examRepository);
        repository2 = new ProfessorRepositoryImpl(professorRepository);
        repository3 = new ExamRepositoryImpl(examRepository);
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

        ProfessorMapper professorMapper = new ProfessorMapper();
        ProfessorEntity professorEntity = professorMapper.toEntity(professor);

        repository2.save(professorEntity);

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);
        Exam exam = new Exam.ExamBuilder()
                .id(null)
                .fachId(null)
                .title("Test 1")
                .professorFachId(professorEntity.getFachId())
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        ExamMapper examMapper = new ExamMapper();
        ExamEntity examEntity = examMapper.toEntity(exam);

        repository3.save(examEntity);

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(null)
                .frageText("Was ist Java?")
                .maxPunkte(6)
                .professorUUID(professorEntity.getFachId())
                .examUUID(exam.getFachId())
                .build();

        FrageMapper frageMapper = new FrageMapper((FrageRepositoryImpl) repository);
        FrageEntity frageEntity = frageMapper.toEntity(frage);

        // Act
        repository.save(frageEntity);
        Optional<FrageEntity> geladen = frageRepository.findByFachId(frageEntity.getFachId());
        ProfessorEntity extraction = ((FrageRepositoryImpl) repository).findByProfFachId(professorEntity.getFachId());

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get().getFrageText()).isEqualTo("Was ist Java?");
        assertThat(geladen.get().getMaxPunkte()).isEqualTo(6);
        assertThat(geladen.get().getFachId()).isEqualTo(frageEntity.getFachId());

        assertThat(extraction.getFachId()).isEqualTo(professorEntity.getFachId());
        assertThat(extraction.getName()).isEqualTo("Dr. Lowkey");

        assertThat(exam.getFachId()).isEqualTo(frageEntity.getExamFachId());
    }
}
