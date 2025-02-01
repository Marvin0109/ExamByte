package exambyte.persistence.container;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.aggregate.exam.Review;
import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.*;
import exambyte.persistence.mapper.*;
import exambyte.persistence.repository.*;
import exambyte.service.*;
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
    private SpringDataKorrektorRepository korrektorRepository;

    @Autowired
    private SpringDataExamRepository eRepository;

    @Autowired
    private SpringDataReviewRepository reviewRepository;

    private FrageRepository frageRepository;
    private AntwortRepository antwortRepository;
    private ProfessorRepository profRepository;
    private StudentRepository studRepository;
    private ExamRepository examRepository;
    private KorrektorRepository korRepository;
    private ReviewRepository revRepository;

    @BeforeEach
    public void setUp() {
        antwortRepository = new AntwortRepositoryImpl(antRepository);
        frageRepository = new FrageRepositoryImpl(professorRepository, frRepository, eRepository);
        profRepository = new ProfessorRepositoryImpl(professorRepository);
        studRepository = new StudentRepositoryImpl(studentRepository);
        examRepository = new ExamRepositoryImpl(eRepository);
        korRepository = new KorrektorRepositoryImpl(korrektorRepository);
        revRepository = new ReviewRepositoryImpl(reviewRepository);
    }

    @Test
    @DisplayName("Relationen der Entitäten testen")
    void test_01() {
        // Arrange
        ProfessorMapper profMapper = new ProfessorMapper();
        KorrektorMapper korrektorMapper = new KorrektorMapper();
        StudentMapper studentMapper = new StudentMapper();
        FrageMapper frageMapper = new FrageMapper((FrageRepositoryImpl) frageRepository);
        AntwortMapper antwortMapper = new AntwortMapper();
        ExamMapper examMapper = new ExamMapper();
        ReviewMapper reviewMapper = new ReviewMapper();

        Professor professor = Professor.of(null, null, "Dr. K");
        Korrektor korrektor = Korrektor.of(null, null, "W Korrektor");
        Student student = Student.of(null, null, "Peter Griffin");

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 20, 8, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 2, 14, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 7, 9, 14, 0);
        Exam exam = Exam.of(null, null, "Test 1", professor.uuid(), startTime, endTime, resultTime);

        Frage frage = Frage.of(
                null,
                null,
                "JPA oder JDBC?",
                7,
                professor.uuid(),
                exam.getFachId());

        LocalDateTime startSubmit = LocalDateTime.of(2025, 6, 22, 10, 23);
        LocalDateTime lastChanges = LocalDateTime.of(2025, 6, 25, 13, 56);
        Antwort antwort = Antwort.of(
                null,
                null,
                "JDBC",
                frage.getFachId(),
                student.uuid(),
                startSubmit,
                lastChanges);

        Review review = Review.of(null, null, antwort.getFachId(), korrektor.uuid(), "Bewertung", 0);

        ProfessorEntity professorEntity = profMapper.toEntity(professor);
        KorrektorEntity korrektorEntity = korrektorMapper.toEntity(korrektor);
        StudentEntity studentEntity = studentMapper.toEntity(student);
        FrageEntity frageEntity = frageMapper.toEntity(frage);
        AntwortEntity antwortEntity = antwortMapper.toEntity(antwort);
        ExamEntity examEntity = examMapper.toEntity(exam);
        ReviewEntity reviewEntity = reviewMapper.toEntity(review);

        studRepository.save(studentEntity);
        profRepository.save(professorEntity);
        korRepository.save(korrektorEntity);
        examRepository.save(examEntity);
        frageRepository.save(frageEntity);
        antwortRepository.save(antwortEntity);
        revRepository.save(reviewEntity);

        // Act

        Optional<AntwortEntity> geladenAntwort = antRepository.findByFachId(antwortEntity.getFachId());
        Optional<FrageEntity> geladenFrage = frageRepository.findByFachId(frageEntity.getFachId());
        Optional<ProfessorEntity> geladenProf = profRepository.findByFachId(professorEntity.getFachId());
        Optional<KorrektorEntity> geladenKor = korRepository.findByFachId(korrektorEntity.getFachId());
        Optional<StudentEntity> geladenStud = studentRepository.findByFachId(studentEntity.getFachId());
        Optional<ExamEntity> geladenExam = examRepository.findByFachId(examEntity.getFachId());
        Optional<ReviewEntity> geladenReview = reviewRepository.findByFachId(reviewEntity.getFachId());

        // Assert

        // Antwort
        assertThat(geladenAntwort).isPresent();
        assertThat(geladenAntwort.get().getAntwortText()).isEqualTo("JDBC");
        assertThat(geladenAntwort.get().getFachId()).isEqualTo(antwortEntity.getFachId());

        // Frage
        assertThat(geladenFrage).isPresent();
        assertThat(geladenFrage.get().getFrageText()).isEqualTo("JPA oder JDBC?");
        assertThat(geladenFrage.get().getMaxPunkte()).isEqualTo(7);
        assertThat(geladenFrage.get().getExamFachId()).isEqualTo(examEntity.getFachId());

        // Professor
        assertThat(geladenProf).isPresent();
        assertThat(geladenProf.get().getName()).isEqualTo("Dr. K");

        // Student
        assertThat(geladenStud).isPresent();
        assertThat(geladenStud.get().getName()).isEqualTo("Peter Griffin");

        // Korrektor
        assertThat(geladenKor).isPresent();
        assertThat(geladenKor.get().getName()).isEqualTo("W Korrektor");

        // Exam
        assertThat(geladenExam).isPresent();
        assertThat(geladenExam.get().getTitle()).isEqualTo("Test 1");
        assertThat(geladenExam.get().getStartZeitpunkt()).isEqualTo(startTime);
        assertThat(geladenExam.get().getEndZeitpunkt()).isEqualTo(endTime);
        assertThat(geladenExam.get().getResultZeitpunkt()).isEqualTo(resultTime);

        // Review
        assertThat(geladenReview).isPresent();
        assertThat(geladenReview.get().getFachId()).isEqualTo(reviewEntity.getFachId());
        assertThat(geladenReview.get().getKorrektorFachId()).isEqualTo(korrektor.uuid());
        assertThat(geladenReview.get().getBewertung()).isEqualTo("Bewertung");
        assertThat(geladenReview.get().getPunkte()).isEqualTo(0);
    }
}
