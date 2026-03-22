package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.aggregate.exam.Answer;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Question;
import exambyte.domain.model.aggregate.exam.CorrectAnswers;
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
    private QuestionRepository questionRepository;

    @Autowired
    private CorrectAnswersRepository correctAnswersRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExamControllerService examControllerService;

    @Test
    void submitAnswers_generateReviews() {
        studentRepository.save(new Student.StudentBuilder().name("Student").build());
        professorRepository.save(new Professor.ProfessorBuilder().name("Professor").build());
        reviewerRepository.save(new Reviewer.ReviewerBuilder()
                .name("Auto reviewer")
                .build());

        Optional<UUID> profId = examControllerService.getProfIdByName("Professor");

        assert(profId.isPresent());

        LocalDateTime start = LocalDateTime.now();

        examRepository.save(new Exam.ExamBuilder()
                .professorId(profId.get())
                .start(start)
                .end(start.plusDays(1))
                .result(start.plusDays(2))
                .title("Exam")
                .build());

        UUID examId = examControllerService.getExamUUIDByStartTime(start);

        questionRepository.save(new Question.FrageBuilder()
                .text("Question")
                .type(QuestionType.SC)
                .points(1)
                .examId(examId)
                .build());

        questionRepository.save(new Question.FrageBuilder()
                .text("Question 2")
                .type(QuestionType.FREE_RESPONSE)
                .points(2)
                .examId(examId)
                .build());

        Optional<UUID> questionIdFreeResponse = questionRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.FREE_RESPONSE))
                .map(Question::getId)
                .findFirst();

        assert(questionIdFreeResponse.isPresent());

        Optional<UUID> questionIdSC = questionRepository.findAll().stream()
                .filter(f -> f.getType().equals(QuestionType.SC))
                .map(Question::getId)
                .findFirst();

        assert(questionIdSC.isPresent());

        correctAnswersRepository.save(new CorrectAnswers.CorrectAnswersBuilder()
                .questionId(questionIdSC.get())
                .choices("A\nB\nC\nD")
                .solution("B")
                .build());

        Map<String, List<String>> answers = new HashMap<>();
        answers.put(questionIdFreeResponse.get().toString(), List.of("Answer"));
        answers.put(questionIdSC.get().toString(), List.of("B"));

        boolean submitted = examControllerService.examIsAlreadySubmitted(examId, "Student");
        assertThat(submitted).isFalse();

        boolean success = examControllerService.submitExam("Student", answers, examId);
        assertThat(success).isTrue();

        Answer answer = answerRepository.findByQuestionId(questionIdSC.get());

        assertThat(answerRepository.findByQuestionId(questionIdFreeResponse.get())).isNotNull();
        assertThat(answerRepository.findByQuestionId(questionIdSC.get())).isNotNull();

        assertThat(reviewRepository.findByAnswerId(answer.getId())).isNotNull();
    }
}
