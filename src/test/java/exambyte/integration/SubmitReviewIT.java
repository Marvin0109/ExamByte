package exambyte.integration;

import exambyte.application.service.ExamControllerService;
import exambyte.domain.model.exam.Answer;
import exambyte.domain.model.exam.Exam;
import exambyte.domain.model.exam.Question;
import exambyte.domain.model.exam.Review;
import exambyte.domain.model.user.Reviewer;
import exambyte.domain.model.user.Professor;
import exambyte.domain.model.user.Student;
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
    private ReviewerRepository reviewerRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

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

        // Reviewer
        Reviewer reviewer = new Reviewer.ReviewerBuilder()
                .name("Reviewer")
                .build();
        reviewerRepository.save(reviewer);
        Optional<Reviewer> reviewerLoaded = reviewerRepository.findByName("Reviewer");
        assertThat(reviewerLoaded).isPresent();

        // Exam
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        Exam exam = new Exam.ExamBuilder()
                .title("Exam")
                .professorId(profLoaded.get().id())
                .start(start)
                .end(start.plusDays(1))
                .result(start.plusDays(2))
                .build();
        examRepository.save(exam);
        Optional<UUID> examId = examRepository.findByStartTime(start);
        assertThat(examId).isPresent();

        // Question
        Question question = new Question.FrageBuilder()
                .examId(examId.get())
                .text("Question")
                .points(5)
                .type(QuestionType.FREE_RESPONSE)
                .build();
        questionRepository.save(question);
        List<Question> questionLoaded = questionRepository.findByExamId(examId.get());
        assertThat(questionLoaded).isNotEmpty();

        // Answer
        Answer answer = new Answer.AnswerBuilder()
                .questionId(questionLoaded.getFirst().getId())
                .answer("Answer")
                .studentId(studentId.get())
                .submitTime(LocalDateTime.of(2026, 1, 1, 1, 0))
                .build();
        answerRepository.save(answer);
        Optional<Answer> answerLoaded = answerRepository
                .findByStudentIdAndQuestionId(studentId.get(), questionLoaded.getFirst().getId());
        assertThat(answerLoaded).isPresent();

        ReviewForm form = new ReviewForm();
        form.setReviewText("Text");
        form.setPoints(5.0);

        examControllerService.createReview(form, answerLoaded.get().getId(),reviewerLoaded.get().id());

        Review review = reviewRepository.findByAnswerId(answerLoaded.get().getId());

        assertThat(review).isNotNull();
        assertThat(review.getPoints()).isEqualTo(5);
    }
}
