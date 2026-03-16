package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.repository.*;
import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.create_exam.QuestionData;
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
class CreateExamIT {

    @Autowired
    private ExamControllerService examControllerService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FrageRepository frageRepository;

    @Autowired
    private KorrekteAntwortenRepository korrekteAntwortenRepository;

    @Test
    void createExamAndQuestions() {
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        System.out.println("Professor: " + professorRepository.findByName("Professor").toString());

        QuestionData q1 = new QuestionData();
        QuestionData q2 = new QuestionData();
        QuestionData q3 = new QuestionData();

        q1.setQuestionText("Question 1");
        q2.setQuestionText("Question 2");
        q3.setQuestionText("Question 3");

        q1.setPunkte(1.5);
        q2.setPunkte(2);
        q3.setPunkte(3);

        q1.setType("SC");
        q2.setType("MC");
        q3.setType("FREITEXT");

        q1.setChoices("A\nB");
        q2.setChoices("A\nB\nC\nD");

        q1.setCorrectAnswer("A");
        q2.setCorrectAnswers("A\nB");

        ExamForm examForm = new ExamForm();
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);

        examForm.setQuestions(List.of(q1, q2, q3));

        examForm.setTitle("Exam 1");
        examForm.setStart(start);
        examForm.setEnd(start.plusDays(1));
        examForm.setResult(start.plusDays(2));

        String createExamMessage = examControllerService.createExam(examForm, "Professor");
        UUID examId = examControllerService.getExamUUIDByStartTime(start);
        Optional<UUID> profID = examControllerService.getProfIdByName("Professor");

        assertThat(profID).isPresent();

        examControllerService.createQuestions(examForm, examId);

        assertThat(createExamMessage).isEmpty();

        assertThat(examRepository.findAll()).hasSize(1);
        assertThat(frageRepository.findAll()).hasSize(3);

        Optional<UUID> frageId = frageRepository.findAll()
                .stream()
                .filter(f -> f.getType().equals(QuestionType.SC))
                .map(Frage::getId)
                .findFirst();

        assertThat(frageId).isPresent();

        assertThat(korrekteAntwortenRepository.findByFrageId(frageId.get())).isPresent();
    }
}
