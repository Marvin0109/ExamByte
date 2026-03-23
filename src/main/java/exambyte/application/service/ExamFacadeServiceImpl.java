package exambyte.application.service;

import exambyte.application.dto.*;
import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.dto.export.ReviewExportDTO;
import exambyte.application.service.export.ExamExportService;
import exambyte.application.service.export.ReviewExportService;
import exambyte.application.service.query.*;
import exambyte.application.service.usecase.ReviewManagementService;
import exambyte.application.service.usecase.ExamManagementService;
import exambyte.application.service.usecase.SubmitExamResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamFacadeServiceImpl implements ExamFacadeService {

    private final ReviewManagementService reviewManagementService;
    private final ExamManagementService examManagementService;
    private final ExamExportService examExportService;
    private final ReviewExportService reviewExportService;
    private final QuestionService questionService;
    private final ProfessorService professorService;
    private final ReviewerService reviewerService;
    private final StudentService studentService;
    private final AnswerService answerService;
    private final ReviewService reviewService;
    private final CorrectAnswersService correctAnswersService;


    public ExamFacadeServiceImpl(ReviewManagementService reviewManagementService,
                                 ExamManagementService examManagementService,
                                 ExamExportService examExportService,
                                 ReviewExportService reviewExportService,
                                 QuestionService questionService,
                                 ProfessorService professorService,
                                 ReviewerService reviewerService,
                                 StudentService studentService,
                                 AnswerService answerService,
                                 ReviewService reviewService,
                                 CorrectAnswersService correctAnswersService) {

        this.reviewManagementService = reviewManagementService;
        this.examManagementService = examManagementService;
        this.examExportService = examExportService;
        this.reviewExportService = reviewExportService;
        this.questionService = questionService;
        this.professorService = professorService;
        this.reviewerService = reviewerService;
        this.studentService = studentService;
        this.answerService = answerService;
        this.reviewService = reviewService;
        this.correctAnswersService = correctAnswersService;
    }

    @Override
    public String createExam(String professorName,
                              String title,
                              LocalDateTime start,
                              LocalDateTime end,
                              LocalDateTime result) {

        return examManagementService.createExam(professorName, title, start, end, result);
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examManagementService.getAllExams();
    }

    @Override
    public boolean isExamAlreadySubmitted(UUID examId, String studentName) {
        return examManagementService.hasStudentSubmittedExam(examId, studentName);
    }

    @Override
    public boolean submitExam(String studentName, Map<String, List<String>> answer, UUID examId) {
        SubmitExamResult result = examManagementService.submitExam(studentName, answer, examId);
        return result.equals(SubmitExamResult.SUCCESS);
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examManagementService.getExam(examId);
    }

    @Override
    public List<QuestionDTO> getQuestionsForExam(UUID examId) {
        return questionService.getQuestionsForExam(examId);
    }

    @Override
    public Optional<UUID> getProfIDByName(String name) {
        return professorService.getProfIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessor(UUID profId) {
        return professorService.getProfessorById(profId);
    }

    @Override
    public void createQuestion(QuestionDTO question) {
        questionService.createQuestion(question);
    }

    @Override
    public void createChoiceQuestion(QuestionDTO question, String correctAnswer, String choices) {
        questionService.createChoiceQuestion(question, correctAnswer, choices);
    }

    @Override
    public String getChoicesForQuestion(UUID questionId) {
         return questionService.getChoiceForQuestion(questionId);
    }

    @Override
    public UUID getExamByStartTime(LocalDateTime start) {
        return examManagementService.getExamIdByStartTime(start);
    }

    @Override
    public boolean deleteById(UUID id) {
        return examManagementService.deleteById(id);
    }

    @Override
    public boolean reset() {
        return examManagementService.resetAllExamDataCascade();
    }

    @Override
    public AttemptDTO getSubmission(UUID examId, String studentName) {
        return examManagementService.getSubmission(examId, studentName);
    }

    @Override
    public void saveAutomaticReviewer() {
        reviewerService.saveAutomaticReviewer();
    }

    @Override
    public double reviewCoverage(UUID examId) {
        return reviewManagementService.getReviewCoverage(examId);
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        return studentService.getStudentSubmittedExam(examId);
    }

    @Override
    public boolean isSubmitBeingReviewed(UUID examId, UUID studentId) {
        return reviewManagementService.submitHasReview(examId, studentId);
    }

    @Override
    public List<QuestionDTO> getFreeResponseQuestions(UUID examId) {
        return questionService.getFreeResponseQuestions(examId);
    }

    @Override
    public List<AnswerDTO> getFreeResponseSolutionForExam(UUID examId) {
        return answerService.getFreeResponseAnswersForExam(examId);
    }

    @Override
    public boolean answerHasReview(AnswerDTO answer) {
       return reviewService.answerHasReview(answer.id());
    }

    @Override
    public void createReview(String text, double points, UUID answerId, UUID reviewerId) {
        reviewService.createReview(text, points, answerId, reviewerId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return reviewerService.getReviewerIdByName(name);
    }

    @Override
    public UUID getStudentIdByName(String name) {
        return studentService.getStudentIdByName(name);
    }

    @Override
    public AnswerDTO getAnswerForQuestionIdAndStudentId(UUID questionId, UUID studentId) {
        return answerService.findByStudentAndQuestion(studentId, questionId);
    }

    @Override
    public ReviewDTO getReviewForAnswer(UUID answerId) {
        return reviewService.getReviewByAnswerId(answerId);
    }

    @Override
    public CorrectAnswersDTO getCorrectAnswerForQuestion(UUID questionId) {
        return correctAnswersService.getCorrectAnswerForQuestion(questionId);
    }

    @Override
    public boolean timeReachedToViewReview(UUID examId) {
        return examManagementService.allowedToViewReview(examId);
    }

    @Override
    public ReviewerDTO getReviewerById(UUID reviewerId) {
        return reviewerService.getReviewerById(reviewerId);
    }

    @Override
    public List<ExamExportDTO> getExamExportDTOs(UUID examId) {
        return examExportService.createExamExport(examId);
    }

    @Override
    public List<ReviewExportDTO> getReviewExportDTOs(UUID examId, String studentName) {
        return reviewExportService.createReviewExport(examId, studentName);
    }
}
