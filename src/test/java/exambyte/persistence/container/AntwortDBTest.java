package exambyte.persistence.container;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;
import exambyte.persistence.mapper.JDBC.AntwortMapperJDBC;
import exambyte.persistence.mapper.JDBC.FrageMapperJDBC;
import exambyte.persistence.mapper.JDBC.ProfessorMapperJDBC;
import exambyte.persistence.mapper.JDBC.StudentMapperJDBC;
import exambyte.persistence.repository.*;
import exambyte.service.AntwortRepository;
import exambyte.service.FrageRepository;
import exambyte.service.ProfessorRepository;
import exambyte.service.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class AntwortDBTest {

    @Autowired
    private SpringDataFrageRepository frRepository;

    @Autowired
    private SpringDataAntwortRepository antRepository;

    @Autowired
    private SpringDataProfessorRepository professorRepository;

    @Autowired
    private SpringDataStudentRepository studentRepository;

    private FrageRepository frageRepository;
    private AntwortRepository antwortRepository;
    private ProfessorRepository profRepository;
    private StudentRepository studRepository;

    @BeforeEach
    public void setUp() {
        antwortRepository = new AntwortRepositoryImpl(antRepository);
        frageRepository = new FrageRepositoryImpl(professorRepository, frRepository);
        profRepository = new ProfessorRepositoryImpl(professorRepository);
        studRepository = new StudentRepositoryImpl(studentRepository);
    }

    @Test
    @DisplayName("Eine Antwort kann gespeichert und geladen werden, die Frage kann auch extrahiert werden")
    void test_01() {
        // Arrange
        ProfessorMapperJDBC profMapper = new ProfessorMapperJDBC();
        StudentMapperJDBC studentMapper = new StudentMapperJDBC();
        FrageMapperJDBC frageMapper = new FrageMapperJDBC(profMapper, (FrageRepositoryImpl) frageRepository);
        AntwortMapperJDBC antwortMapper = new AntwortMapperJDBC();

        Professor professor = Professor.of(null, null, "Dr.K");
        Student student = Student.of(null, null, "Peter Griffin");
        Frage frage = Frage.of(null, null, "JPA oder JDBC?", professor.uuid());
        Antwort antwort = Antwort.of(null, null, "JDBC", false, frage.getFachId(), student.uuid());

        ProfessorEntityJDBC professorEntity = profMapper.toEntity(professor);
        StudentEntityJDBC studentEntity = studentMapper.toEntity(student);
        FrageEntityJDBC frageEntity = frageMapper.toEntity(frage);
        AntwortEntityJDBC antwortEntity = antwortMapper.toEntity(antwort);

        studRepository.save(studentEntity);
        profRepository.save(professorEntity);
        frageRepository.save(frageEntity);
        antwortRepository.save(antwortEntity);

        // Act
        Optional<AntwortEntityJDBC> geladenAntwort = antRepository.findByFachId(antwortEntity.getFachId());
        Optional<FrageEntityJDBC> geladenFrage = frageRepository.findByFachId(frageEntity.getFachId());

        // Assert
        assertThat(geladenAntwort).isPresent();
        assertThat(geladenAntwort.get().getAntwortText()).isEqualTo("JDBC");
        assertThat(geladenAntwort.get().getFachId()).isEqualTo(antwortEntity.getFachId());
        assertThat(geladenFrage).isPresent();
        assertThat(geladenFrage.get().getFrageText()).isEqualTo("JPA oder JDBC?");
    }

}
