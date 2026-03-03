package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.*;
import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import exambyte.web.form.create_review.ReviewForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(TestcontainerConfiguration.class)
class SubmitReviewIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private KorrektorRepository korrektorRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FrageRepository frageRepository;

    @Autowired
    private AntwortRepository antwortRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExamControllerService examControllerService;

    @Test
    void submitReview() {
        // Student
        Student student = new Student.StudentBuilder()
                .name("Student")
                .build();
        studentRepository.save(student);
        Optional<UUID> studentId = studentRepository.findIdByName(student.getName());
        assertThat(studentId).isPresent();

        // Professor
        Professor professor = new Professor.ProfessorBuilder()
                .name("Professor")
                .build();
        professorRepository.save(professor);
        Optional<Professor> profLoaded = professorRepository.findByName("Professor");
        assertThat(profLoaded).isPresent();

        // Korrektor
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .name("Korrektor")
                .build();
        korrektorRepository.save(korrektor);
        Optional<Korrektor> korrektorLoaded = korrektorRepository.findByName("Korrektor");
        assertThat(korrektorLoaded).isPresent();

        // Exam
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        Exam exam = new Exam.ExamBuilder()
                .title("Exam")
                .professorId(profLoaded.get().id())
                .startTime(start)
                .endTime(start.plusDays(1))
                .resultTime(start.plusDays(2))
                .build();
        examRepository.save(exam);
        Optional<UUID> examId = examRepository.findByStartTime(start);
        assertThat(examId).isPresent();

        // Frage
        Frage frage = new Frage.FrageBuilder()
                .examId(examId.get())
                .frageText("Frage")
                .maxPunkte(5)
                .type(QuestionType.FREITEXT)
                .build();
        frageRepository.save(frage);
        List<Frage> frageLoaded = frageRepository.findByExamId(examId.get());
        assertThat(frageLoaded).isNotEmpty();

        // Antwort
        Antwort antwort = new Antwort.AntwortBuilder()
                .frageId(frageLoaded.getFirst().getId())
                .antwortText("Antwort")
                .studentId(studentId.get())
                .antwortZeitpunkt(LocalDateTime.of(2026, 1, 1, 1, 0))
                .build();
        antwortRepository.save(antwort);
        Optional<Antwort> antwortLoaded = antwortRepository
                .findByStudentIdAndFrageId(studentId.get(), frageLoaded.getFirst().getId());
        assertThat(antwortLoaded).isPresent();

        ReviewForm form = new ReviewForm();
        form.setBewertung("Bewertung");
        form.setPunkteVergeben(5);

        examControllerService.createReview(form, antwortLoaded.get().getId(), korrektorLoaded.get().id());

        Review review = reviewRepository.findByAntwortId(antwortLoaded.get().getId());

        assertThat(review).isNotNull();
        assertThat(review.getPunkte()).isEqualTo(5);
    }
}
