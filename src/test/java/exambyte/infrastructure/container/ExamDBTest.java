package exambyte.infrastructure.container;

import exambyte.domain.model.exam.Exam;
import exambyte.domain.repository.*;
import exambyte.infrastructure.mapper.ExamMapper;
import exambyte.infrastructure.repository.ExamDAO;
import exambyte.infrastructure.repository.ExamRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
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
    private ExamDAO dao;

    private ExamRepository repository;

    private static final UUID EXAM_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @BeforeEach
    void setUp() {
        ExamMapper examMapper = new ExamMapper();
        repository = new ExamRepositoryImpl(dao, examMapper);
    }

    @Test
    void load_data_success() {
        // Act
        Optional<Exam> loaded = repository.findById(EXAM_ID);

        // Assert
        assertThat(loaded).isPresent();
    }

    @Test
    void deleteAll_success() {
        // Act
        repository.deleteAll();

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_success() {
        // Act
        repository.deleteById(EXAM_ID);

        // Assert
        assertThat(repository.findById(EXAM_ID)).isEmpty();
    }

    @Test
    void find_exam_by_startTime() {
        // Act
        Optional<UUID> loaded = repository.findByStartTime(
                LocalDateTime.of(2025, 6, 20, 8, 0, 0));

        // Assert
        assertThat(loaded).contains(EXAM_ID);
    }
}
