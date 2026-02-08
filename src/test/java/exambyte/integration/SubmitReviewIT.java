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
        Student student = new Student.StudentBuilder()
                .fachId(UUID.randomUUID())
                .name("Student")
                .build();
        studentRepository.save(student);

        Professor professor = new Professor.ProfessorBuilder()
                .fachId(UUID.randomUUID())
                .name("Professor")
                .build();
        professorRepository.save(professor);

        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .fachId(UUID.randomUUID())
                .name("Korrektor")
                .build();
        korrektorRepository.save(korrektor);

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        Exam exam = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title("Exam")
                .professorFachId(professor.uuid())
                .startTime(start)
                .endTime(start.plusDays(1))
                .resultTime(start.plusDays(2))
                .build();
        examRepository.save(exam);

        Frage frage = new Frage.FrageBuilder()
                .fachId(UUID.randomUUID())
                .examUUID(exam.getFachId())
                .frageText("Frage")
                .maxPunkte(5)
                .professorUUID(professor.uuid())
                .type(QuestionType.FREITEXT)
                .build();
        frageRepository.save(frage);

        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(UUID.randomUUID())
                .frageFachId(frage.getFachId())
                .antwortText("Antwort")
                .studentFachId(student.uuid())
                .antwortZeitpunkt(LocalDateTime.of(2026, 1, 1, 1, 0))
                .build();
        antwortRepository.save(antwort);

        ReviewForm form = new ReviewForm();
        form.setBewertung("Bewertung");
        form.setPunkteVergeben(5);

        examControllerService.createReview(form, antwort.getFachId(), korrektor.uuid());

        Review review = reviewRepository.findByAntwortFachId(antwort.getFachId());

        assertThat(review).isNotNull();
        assertThat(review.getPunkte()).isEqualTo(5);
    }
}
