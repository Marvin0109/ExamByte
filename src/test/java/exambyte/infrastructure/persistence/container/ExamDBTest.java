package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.exam.*;
import exambyte.domain.entitymapper.*;
import exambyte.domain.repository.*;
import exambyte.infrastructure.persistence.mapper.*;
import exambyte.infrastructure.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql(scripts = "/data-test.sql")
class ExamDBTest {

    @Autowired
    private ExamDAO examDAO;

    private ExamRepository examRepository;

    private static final UUID EXAMUUID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @BeforeEach
    void setUp() {
        ExamMapper examMapper = new ExamMapperImpl();
        examRepository = new ExamRepositoryImpl(examDAO, examMapper);
    }

    @Test
    @DisplayName("Laden der Daten erfolgreich")
    void test_01() {
        // Act
        Optional<Exam> geladenExam = examRepository.findByFachId(EXAMUUID);

        // Assert
        assertThat(geladenExam).isPresent();
    }

    @Test
    @DisplayName("Alle Daten in der Tabelle löschen")
    void test_02() {
        // Act
        examRepository.deleteAll();

        // Assert
        assertThat(examRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteByFachId")
    void test_03() {
        // Act
        examRepository.deleteByFachId(EXAMUUID);

        // Assert
        assertThat(examRepository.findByFachId(EXAMUUID)).isEmpty();
    }

    @Test
    @DisplayName("Suche Exam nach Startzeit")
    void test_04() {
        // Act
        Optional<UUID> geladen = examRepository.findByStartTime(
                LocalDateTime.of(2025, 6, 20, 8, 0, 0));

        // Assert
        assertThat(geladen).contains(EXAMUUID);
    }
}
