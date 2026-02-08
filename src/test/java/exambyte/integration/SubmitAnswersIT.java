package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.KorrekteAntworten;
import exambyte.domain.model.aggregate.user.Korrektor;
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
    private KorrektorRepository korrektorRepository;

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
        korrektorRepository.save(new Korrektor.KorrektorBuilder()
                .fachId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .name("Automatischer Korrektor")
                .build());

        Optional<UUID> profId = examControllerService.getProfFachIDByName("Professor");

        assert(profId.isPresent());

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        examRepository.save(new Exam.ExamBuilder()
                .professorFachId(profId.get())
                .startTime(start)
                .endTime(start.plusDays(1))
                .resultTime(start.plusDays(2))
                .title("Exam")
                .build());

        UUID examId = examControllerService.getExamUUIDByStartTime(start);

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage")
                .professorUUID(profId.get())
                .type(QuestionType.SC)
                .maxPunkte(1)
                .examUUID(examId)
                .build());

        frageRepository.save(new Frage.FrageBuilder()
                .frageText("Frage 2")
                .professorUUID(profId.get())
                .type(QuestionType.FREITEXT)
                .maxPunkte(2)
                .examUUID(examId)
                .build());

        Optional<UUID> frageIdFreitext = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.FREITEXT))
                .map(Frage::getFachId)
                .findFirst();

        assert(frageIdFreitext.isPresent());

        Optional<UUID> frageIdSC = frageRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.SC))
                .map(Frage::getFachId)
                .findFirst();

        assert(frageIdSC.isPresent());

        korrekteAntwortenRepository.save(new KorrekteAntworten.KorrekteAntwortenBuilder()
                .frageFachId(frageIdSC.get())
                .antwortOptionen("A\nB\nC\nD")
                .loesungen("B")
                .build());

        Map<String, List<String>> answers = new HashMap<>();
        answers.put(frageIdFreitext.get().toString(), List.of("Antwort Text"));
        answers.put(frageIdSC.get().toString(), List.of("B"));

        boolean submitted = examControllerService.examIsAlreadySubmitted(examId, "Student");
        assertThat(submitted).isFalse();

        boolean success = examControllerService.submitExam("Student", answers, examId);
        assertThat(success).isTrue();

        Antwort antwort = antwortRepository.findByFrageFachId(frageIdSC.get());

        assertThat(antwortRepository.findByFrageFachId(frageIdFreitext.get())).isNotNull();
        assertThat(antwortRepository.findByFrageFachId(frageIdSC.get())).isNotNull();

        assertThat(reviewRepository.findByAntwortFachId(antwort.getFachId())).isNotNull();
    }
}
