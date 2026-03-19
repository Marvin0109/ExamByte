package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.model.aggregate.user.Reviewer;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.*;
import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Import(TestcontainerConfiguration.class)
class SubmitAnswersIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FrageRepository frageRepository;

    @Autowired
    private KorrekteAntwortenRepository korrekteAntwortenRepository;

    @Autowired
    private AntwortRepository antwortRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExamControllerService examControllerService;

    @Test
    void submitAnswers_generateReviews() {
        studentRepository.save(new Student.StudentBuilder().name("Student").build());
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        reviewerRepository.save(new Reviewer.ReviewerBuilder()
                .name("Automatischer Reviewer")
                .build());

        Optional<UUID> profId = examControllerService.getProfIdByName("Professor");

        assert(profId.isPresent());

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        examRepository.save(new Exam.ExamBuilder()
                .professorId(profId.get())
                .startTime(start)
                .endTime(start.plusDays(1))
                .resultTime(start.plusDays(2))
                .title("Exam")
                .build());

        UUID examId = examControllerService.getExamUUIDByStartTime(start);

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage")
                .type(QuestionType.SC)
                .maxPunkte(1)
                .examId(examId)
                .build());

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage 2")
                .type(QuestionType.FREE_RESPONSE)
                .maxPunkte(2)
                .examId(examId)
                .build());

        Optional<UUID> frageIdFreeResponse = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.FREE_RESPONSE))
                .map(Frage::getId)
                .findFirst();

        assert(frageIdFreeResponse.isPresent());

        Optional<UUID> frageIdSC = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.SC))
                .map(Frage::getId)
                .findFirst();

        assert(frageIdSC.isPresent());

        korrekteAntwortenRepository.save(new KorrekteAntworten.KorrekteAntwortenBuilder()
                .frageId(frageIdSC.get())
                .antwortOptionen("A\nB\nC\nD")
                .loesungen("B")
                .build());

        Map<String, List<String>> answers = new HashMap<>();
        answers.put(frageIdFreeResponse.get().toString(), List.of("Antwort Text"));
        answers.put(frageIdSC.get().toString(), List.of("B"));

        boolean submitted = examControllerService.examIsAlreadySubmitted(examId, "Student");
        assertThat(submitted).isFalse();

        boolean success = examControllerService.submitExam("Student", answers, examId);
        assertThat(success).isTrue();

        Antwort antwort = antwortRepository.findByFrageId(frageIdSC.get());

        assertThat(antwortRepository.findByFrageId(frageIdFreeResponse.get())).isNotNull();
        assertThat(antwortRepository.findByFrageId(frageIdSC.get())).isNotNull();

        assertThat(reviewRepository.findByAntwortId(antwort.getId())).isNotNull();
    }
}
