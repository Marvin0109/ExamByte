package exambyte.infrastructure.persistence.container;

import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.aggregate.exam.*;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
public class ExamDBTest {

    @Autowired
    private FrageDAO frageDAO;

    @Autowired
    private AntwortDAO antwortDAO;

    @Autowired
    private ProfessorDAO professorDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private KorrektorDAO korrektorDAO;

    @Autowired
    private ExamDAO examDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private KorrekteAntwortenDAO korrekteAntwortenDAO;

    private FrageRepository frageRepository;
    private AntwortRepository antwortRepository;
    private ProfessorRepository professorRepository;
    private StudentRepository studentRepository;
    private ExamRepository examRepository;
    private KorrektorRepository korrektorRepository;
    private ReviewRepository reviewRepository;
    private KorrekteAntwortenRepository korrekteAntwortenRepository;

    @BeforeEach
    public void setUp() {
        AntwortMapper antMapper = new AntwortMapperImpl();
        FrageMapper frageMapper = new FrageMapperImpl();
        ProfessorMapper profMapper = new ProfessorMapperImpl();
        StudentMapper studentMapper = new StudentMapperImpl();
        KorrektorMapper korrektorMapper = new KorrektorMapperImpl();
        ExamMapper examMapper = new ExamMapperImpl();
        ReviewMapper reviewMapper = new ReviewMapperImpl();
        KorrekteAntwortenMapper korrekteAntwortenMapper = new KorrekteAntwortenMapperImpl();

        antwortRepository = new AntwortRepositoryImpl(antwortDAO, antMapper);
        frageRepository = new FrageRepositoryImpl(professorDAO, frageDAO, frageMapper);
        professorRepository = new ProfessorRepositoryImpl(professorDAO, profMapper);
        studentRepository = new StudentRepositoryImpl(studentDAO, studentMapper);
        examRepository = new ExamRepositoryImpl(examDAO, examMapper);
        korrektorRepository = new KorrektorRepositoryImpl(korrektorDAO, korrektorMapper);
        reviewRepository = new ReviewRepositoryImpl(reviewDAO, reviewMapper);
        korrekteAntwortenRepository = new KorrekteAntwortenRepositoryImpl(korrekteAntwortenDAO, korrekteAntwortenMapper);
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
                .type(QuestionType.FREITEXT)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        KorrekteAntworten korrekteAntworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(null)
                .fachId(null)
                .frageFachId(frage.getFachId())
                .korrekteAntworten("JDBC")
                .antwort_optionen("JPA\nJDBC")
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

        studentRepository.save(student);
        professorRepository.save(professor);
        korrektorRepository.save(korrektor);
        examRepository.save(exam);
        frageRepository.save(frage);
        antwortRepository.save(antwort);
        reviewRepository.save(review);
        korrekteAntwortenRepository.save(korrekteAntworten);

        // Act

        Optional<Antwort> geladenAntwort = antwortRepository.findByFachId(antwort.getFachId());
        Optional<Frage> geladenFrage = frageRepository.findByFachId(frage.getFachId());
        Optional<Professor> geladenProf = professorRepository.findByFachId(professor.uuid());
        Optional<Korrektor> geladenKor = korrektorRepository.findByFachId(korrektor.uuid());
        Optional<Student> geladenStud = studentRepository.findByFachId(student.uuid());
        Optional<Exam> geladenExam = examRepository.findByFachId(exam.getFachId());
        Optional<Review> geladenReview = reviewRepository.findByFachId(review.getFachId());
        Optional<KorrekteAntworten> geladenKorrekteAntworten = korrekteAntwortenRepository.findByFachId(korrekteAntworten.getFachId());

        // Assert

        // Antwort
        assertThat(geladenAntwort).isPresent();
        assertThat(geladenAntwort.get().getAntwortText()).isEqualTo("JDBC");
        assertThat(geladenAntwort.get().getFachId()).isEqualTo(antwort.getFachId());

        // Frage
        assertThat(geladenFrage).isPresent();
        assertThat(geladenFrage.get().getFrageText()).isEqualTo("JPA oder JDBC?");
        assertThat(geladenFrage.get().getMaxPunkte()).isEqualTo(7);
        assertThat(geladenFrage.get().getType()).isEqualTo(QuestionType.FREITEXT);
        assertThat(geladenFrage.get().getExamUUID()).isEqualTo(exam.getFachId());

        // KorrekteAntworten
        assertThat(geladenKorrekteAntworten).isPresent();
        assertThat(geladenKorrekteAntworten.get().getFachId()).isEqualTo(korrekteAntworten.getFachId());
        assertThat(geladenKorrekteAntworten.get().getFrageFachId()).isEqualTo(frage.getFachId());
        assertThat(geladenKorrekteAntworten.get().getKorrekteAntworten()).contains("JDBC");
        assertThat(geladenKorrekteAntworten.get().getAntwort_optionen()).contains("JPA\nJDBC");

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

    @Test
    @DisplayName("Teste deleteAll/ Alle Daten in der Tabelle löschen")
    void test_02() {
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
                .type(QuestionType.SC)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        KorrekteAntworten korrekteAntworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(null)
                .fachId(null)
                .frageFachId(frage.getFachId())
                .korrekteAntworten("JDBC")
                .antwort_optionen("JPA\nJDBC")
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

        studentRepository.save(student);
        professorRepository.save(professor);
        korrektorRepository.save(korrektor);
        examRepository.save(exam);
        frageRepository.save(frage);
        antwortRepository.save(antwort);
        reviewRepository.save(review);
        korrekteAntwortenRepository.save(korrekteAntworten);

        // Act (Reihenfolge wichtig!)
        reviewRepository.deleteAll();
        antwortRepository.deleteAll();
        korrekteAntwortenRepository.deleteAll();
        frageRepository.deleteAll();
        examRepository.deleteAll();

        // Assert
        assertThat(examRepository.findAll()).isEmpty();
        assertThat(frageRepository.findAll()).isEmpty();
        assertThat(reviewRepository.findByFachId(review.getFachId())).isEmpty();
        assertThat(korrekteAntwortenRepository.findByFachId(korrekteAntworten.getFachId())).isEmpty();
        assertThat(antwortRepository.findByFachId(antwort.getFachId())).isEmpty();
    }

    @Test
    @DisplayName("deleteByFachId (Delete on cascade Test)")
    void test_03() {
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
                .type(QuestionType.MC)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        KorrekteAntworten korrekteAntworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(null)
                .fachId(null)
                .frageFachId(frage.getFachId())
                .korrekteAntworten("JDBC")
                .antwort_optionen("JPA\nJDBC")
                .build();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(null)
                .antwortText("JDBC")
                .frageFachId(frage.getFachId())
                .studentFachId(student.uuid())
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        Review review = new Review.ReviewBuilder()
                .id(null)
                .fachId(null)
                .antwortFachId(antwort.getFachId())
                .korrektorFachId(korrektor.uuid())
                .bewertung("Bewertung")
                .punkte(0)
                .build();

        studentRepository.save(student);
        professorRepository.save(professor);
        korrektorRepository.save(korrektor);
        examRepository.save(exam);
        frageRepository.save(frage);
        antwortRepository.save(antwort);
        reviewRepository.save(review);
        korrekteAntwortenRepository.save(korrekteAntworten);

        // Act
        examRepository.deleteByFachId(exam.getFachId());

        // Assert – abhängige Daten weg
        assertThat(frageRepository.findByFachId(frage.getFachId())).isEmpty();
        assertThat(reviewRepository.findByFachId(review.getFachId())).isEmpty();
        assertThat(korrekteAntwortenRepository.findByFachId(korrekteAntworten.getFachId())).isEmpty();
        assertThat(examRepository.findByFachId(exam.getFachId())).isEmpty();

        // Assert – unabhängige Entities noch vorhanden
        assertThat(professorRepository.findByFachId(professor.uuid())).isPresent();
        assertThat(studentRepository.findByFachId(student.uuid())).isPresent();
        assertThat(korrektorRepository.findByFachId(korrektor.uuid())).isPresent();
    }

    @Test
    @DisplayName("Suche Exam nach Startzeit")
    void test_04() {
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
                .type(QuestionType.SC)
                .professorUUID(professor.uuid())
                .examUUID(exam.getFachId())
                .build();

        KorrekteAntworten korrekteAntworten = new KorrekteAntworten.KorrekteAntwortenBuilder()
                .id(null)
                .fachId(null)
                .frageFachId(frage.getFachId())
                .korrekteAntworten("JDBC")
                .antwort_optionen("JPA\nJDBC")
                .build();

        Antwort antwort = new Antwort.AntwortBuilder()
                .id(null)
                .fachId(null)
                .antwortText("JDBC")
                .frageFachId(frage.getFachId())
                .studentFachId(student.uuid())
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        Review review = new Review.ReviewBuilder()
                .id(null)
                .fachId(null)
                .antwortFachId(antwort.getFachId())
                .korrektorFachId(korrektor.uuid())
                .bewertung("Bewertung")
                .punkte(0)
                .build();

        studentRepository.save(student);
        professorRepository.save(professor);
        korrektorRepository.save(korrektor);
        examRepository.save(exam);
        frageRepository.save(frage);
        antwortRepository.save(antwort);
        reviewRepository.save(review);
        korrekteAntwortenRepository.save(korrekteAntworten);

        // Act
        Optional<UUID> geladen = examRepository.findByStartTime(startTime);

        // Assert
        assertThat(geladen.isPresent()).isTrue();
        assertThat(geladen.get()).isEqualTo(exam.getFachId());
    }
}
