package exambyte.integration;

import exambyte.web.service.ExamControllerService;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.user.Professor;
import exambyte.domain.model.enums.QuestionType;
import exambyte.domain.repository.*;
import exambyte.infrastructure.container.TestcontainerConfiguration;
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
    private QuestionRepository questionRepository;

    @Autowired
    private CorrectAnswersRepository correctAnswersRepository;

    @Test
    void createExamAndQuestions() {
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        System.out.println("Professor: " + professorRepository.findByName("Professor").toString());

        QuestionData q1 = new QuestionData();
        QuestionData q2 = new QuestionData();
        QuestionData q3 = new QuestionData();

        q1.setText("Question 1");
        q2.setText("Question 2");
        q3.setText("Question 3");

        q1.setPoints(1.5);
        q2.setPoints(2.0);
        q3.setPoints(3.0);

        q1.setType("SC");
        q2.setType("MC");
        q3.setType("FREE_RESPONSE");

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
        assertThat(questionRepository.findAll()).hasSize(3);

        Optional<UUID> questionId = questionRepository.findAll()
                .stream()
                .filter(q -> q.getType().equals(QuestionType.SC))
                .map(Question::getId)
                .findFirst();

        assertThat(questionId).isPresent();

        assertThat(correctAnswersRepository.findByQuestionId(questionId.get())).isPresent();
    }
}
