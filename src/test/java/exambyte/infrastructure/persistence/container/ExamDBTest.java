package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Student;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class ExamDBTest {

    @Autowired
    private FrageDAO frRepository;

    @Autowired
    private AntwortDAO antRepository;

    @Autowired
    private ProfessorDAO professorRepository;

    @Autowired
    private StudentDAO studentRepository;

    @Autowired
    private KorrektorDAO korrektorRepository;

    @Autowired
    private ExamDAO eRepository;

    @Autowired
    private ReviewDAO reviewRepository;

    private FrageRepository frageRepository;
    private AntwortRepository antwortRepository;
    private ProfessorRepository profRepository;
    private StudentRepository studRepository;
    private ExamRepository examRepository;
    private KorrektorRepository korRepository;
    private ReviewRepository revRepository;

    @BeforeEach
    public void setUp() {
        AntwortMapper antMapper = new AntwortMapperImpl();
        FrageMapper frageMapper = new FrageMapperImpl();
        ProfessorMapper profMapper = new ProfessorMapperImpl();
        StudentMapper studentMapper = new StudentMapperImpl();
        KorrektorMapper korrektorMapper = new KorrektorMapperImpl();
        ExamMapper examMapper = new ExamMapperImpl();
        ReviewMapper reviewMapper = new ReviewMapperImpl();

        antwortRepository = new AntwortRepositoryImpl(antRepository, antMapper);
        frageRepository = new FrageRepositoryImpl(professorRepository, frRepository, frageMapper);
        profRepository = new ProfessorRepositoryImpl(professorRepository, profMapper);
        studRepository = new StudentRepositoryImpl(studentRepository, studentMapper);
        examRepository = new ExamRepositoryImpl(eRepository, examMapper);
        korRepository = new KorrektorRepositoryImpl(korrektorRepository, korrektorMapper);
        revRepository = new ReviewRepositoryImpl(reviewRepository, reviewMapper);
    }

    @Test
    @DisplayName("Relationen der Entitäten testen")
    void test_01() {
        // Arrange

        Professor professor = new Professor.ProfessorBuilder()
                .id(null)
                .fachId(null)
                .name("Dr. K")
                .build();

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name("W Korrektor")
                .build();

        Student student = new Student.StudentBuilder()
                .id(null)
                .fachId(null)
                .name("Peter Griffin")
                .build();

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

        Frage frage = new Frage.FrageBuilder()
                .id(null)
                .fachId(null)
                .frageText("JPA oder JDBC?")
                .maxPunkte(7)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        LocalDateTime startSubmit = LocalDateTime.of(2025, 6, 22, 10, 23);
        LocalDateTime lastChanges = LocalDateTime.of(2025, 6, 25, 13, 56);
        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(null)
                .antwortText("JDBC")
                .frageFachId(frage.getFachId())
                .studentFachId(student.uuid())
                .antwortZeitpunkt(startSubmit)
                .lastChangesZeitpunkt(lastChanges)
                .build();

        Review review = new Review.ReviewBuilder()
                .id(null)
                .fachId(null)
                .antwortFachId(antwort.getFachId())
                .korrektorFachId(korrektor.uuid())
                .bewertung("Bewertung")
                .punkte(0)
                .build();

        studRepository.save(student);
        profRepository.save(professor);
        korRepository.save(korrektor);
        examRepository.save(exam);
        frageRepository.save(frage);
        antwortRepository.save(antwort);
        revRepository.save(review);

        // Act

        Optional<Antwort> geladenAntwort = antwortRepository.findByFachId(antwort.getFachId());
        Optional<Frage> geladenFrage = frageRepository.findByFachId(frage.getFachId());
        Optional<Professor> geladenProf = profRepository.findByFachId(professor.uuid());
        Optional<Korrektor> geladenKor = korRepository.findByFachId(korrektor.uuid());
        Optional<Student> geladenStud = studRepository.findByFachId(student.uuid());
        Optional<Exam> geladenExam = examRepository.findByFachId(exam.getFachId());
        Optional<Review> geladenReview = revRepository.findByFachId(review.getFachId());

        // Assert

        // Antwort
        assertThat(geladenAntwort).isPresent();
        assertThat(geladenAntwort.get().getAntwortText()).isEqualTo("JDBC");
        assertThat(geladenAntwort.get().getFachId()).isEqualTo(antwort.getFachId());

        // Frage
        assertThat(geladenFrage).isPresent();
        assertThat(geladenFrage.get().getFrageText()).isEqualTo("JPA oder JDBC?");
        assertThat(geladenFrage.get().getMaxPunkte()).isEqualTo(7);
        assertThat(geladenFrage.get().getExamUUID()).isEqualTo(exam.getFachId());

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
        assertThat(geladenExam.get().getStartTime()).isEqualTo(startTime);
        assertThat(geladenExam.get().getEndTime()).isEqualTo(endTime);
        assertThat(geladenExam.get().getResultTime()).isEqualTo(resultTime);

        // Review
        assertThat(geladenReview).isPresent();
        assertThat(geladenReview.get().getFachId()).isEqualTo(review.getFachId());
        assertThat(geladenReview.get().getKorrektorFachId()).isEqualTo(korrektor.uuid());
        assertThat(geladenReview.get().getBewertung()).isEqualTo("Bewertung");
        assertThat(geladenReview.get().getPunkte()).isEqualTo(0);
    }
}
