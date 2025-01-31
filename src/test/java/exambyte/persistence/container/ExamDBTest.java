package exambyte.persistence.container;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JDBC.*;
import exambyte.persistence.mapper.JDBC.*;
import exambyte.persistence.repository.*;
import exambyte.service.*;
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
public class ExamDBTest {

    @Autowired
    private SpringDataFrageRepository frRepository;

    @Autowired
    private SpringDataAntwortRepository antRepository;

    @Autowired
    private SpringDataProfessorRepository professorRepository;

    @Autowired
    private SpringDataStudentRepository studentRepository;

    @Autowired
    private SpringDataExamRepository eRepository;

    private FrageRepository frageRepository;
    private AntwortRepository antwortRepository;
    private ProfessorRepository profRepository;
    private StudentRepository studRepository;
    private ExamRepository examRepository;

    @BeforeEach
    public void setUp() {
        antwortRepository = new AntwortRepositoryImpl(antRepository);
        frageRepository = new FrageRepositoryImpl(professorRepository, frRepository, eRepository);
        profRepository = new ProfessorRepositoryImpl(professorRepository);
        studRepository = new StudentRepositoryImpl(studentRepository);
        examRepository = new ExamRepositoryImpl(eRepository);
    }

    @Test
    @DisplayName("Relationen der Entitäten testen")
    void test_01() {
        // Arrange
        ProfessorMapperJDBC profMapper = new ProfessorMapperJDBC();
        StudentMapperJDBC studentMapper = new StudentMapperJDBC();
        FrageMapperJDBC frageMapper = new FrageMapperJDBC((FrageRepositoryImpl) frageRepository);
        AntwortMapperJDBC antwortMapper = new AntwortMapperJDBC();
        ExamMapperJDBC examMapper = new ExamMapperJDBC();

        Professor professor = Professor.of(null, null, "Dr.K");
        Student student = Student.of(null, null, "Peter Griffin");
        Exam exam = Exam.of(null, null, "Test 1", professor.uuid());
        Frage frage = Frage.of(null, null, "JPA oder JDBC?", professor.uuid(), exam.getFachId());
        Antwort antwort = Antwort.of(null, null, "JDBC", false, frage.getFachId(), student.uuid());

        ProfessorEntityJDBC professorEntity = profMapper.toEntity(professor);
        StudentEntityJDBC studentEntity = studentMapper.toEntity(student);
        FrageEntityJDBC frageEntity = frageMapper.toEntity(frage);
        AntwortEntityJDBC antwortEntity = antwortMapper.toEntity(antwort);
        ExamEntityJDBC examEntity = examMapper.toEntity(exam);

        studRepository.save(studentEntity);
        profRepository.save(professorEntity);
        examRepository.save(examEntity);
        frageRepository.save(frageEntity);
        antwortRepository.save(antwortEntity);

        // Act

        Optional<AntwortEntityJDBC> geladenAntwort = antRepository.findByFachId(antwortEntity.getFachId());
        Optional<FrageEntityJDBC> geladenFrage = frageRepository.findByFachId(frageEntity.getFachId());
        Optional<ProfessorEntityJDBC> geladenProf = profRepository.findByFachId(professorEntity.getFachId());
        Optional<StudentEntityJDBC> geladenStud = studentRepository.findByFachId(studentEntity.getFachId());
        Optional<ExamEntityJDBC> geladenExam = examRepository.findByFachId(examEntity.getFachId());

        // Assert
        assertThat(geladenAntwort).isPresent();
        assertThat(geladenAntwort.get().getAntwortText()).isEqualTo("JDBC");
        assertThat(geladenAntwort.get().getFachId()).isEqualTo(antwortEntity.getFachId());

        assertThat(geladenFrage).isPresent();
        assertThat(geladenFrage.get().getFrageText()).isEqualTo("JPA oder JDBC?");
        assertThat(geladenFrage.get().getExamFachId()).isEqualTo(examEntity.getFachId());

        assertThat(geladenProf).isPresent();
        assertThat(geladenProf.get().getName()).isEqualTo("Dr.K");

        assertThat(geladenStud).isPresent();
        assertThat(geladenStud.get().getName()).isEqualTo("Peter Griffin");

        assertThat(geladenExam).isPresent();
        assertThat(geladenExam.get().getTitle()).isEqualTo("Test 1");
    }
}
