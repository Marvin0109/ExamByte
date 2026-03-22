package exambyte.application.service;

import exambyte.application.dto.*;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamFacadeService {

    String createExam(String profName, String title,
                       LocalDateTime start, LocalDateTime end, LocalDateTime result);

    List<ExamDTO> getAllExams();

    boolean isExamAlreadySubmitted(UUID examId, String studentName);

    boolean submitExam(String studentName, Map<String, List<String>> answer, UUID examId);

    ExamDTO getExam(UUID examId);

    List<QuestionDTO> getQuestionsForExam(UUID examId);

    Optional<UUID> getProfIDByName(String name);

    ProfessorDTO getProfessor(UUID profId);

    void createQuestion(QuestionDTO question);

    void createChoiceQuestion(QuestionDTO question, String correctAnswer, String choices);

    String getChoicesForQuestion(UUID questionId);

    UUID getExamByStartTime(LocalDateTime start);

    boolean deleteById(UUID examId);

    boolean reset();

    AttemptDTO getSubmission(UUID examId, String studentName);

    void saveAutomaticReviewer();

    double reviewCoverage(UUID examId);

    List<StudentDTO> getStudentSubmittedExam(UUID examId);

    boolean isSubmitBeingReviewed(UUID examId, UUID studentId);

    List<QuestionDTO> getFreeResponseQuestions(UUID examId);

    List<AnswerDTO> getFreeResponseSolutionForExam(UUID examId);

    boolean answerHasReview(AnswerDTO answer);

    void createReview(String text, double points, UUID answerId, UUID reviewerId);

    UUID getReviewerByName(String name);

    UUID getStudentIdByName(String name);

    AnswerDTO getAnswerForQuestionIdAndStudentId(UUID frageId, UUID studentId);

    ReviewDTO getReviewForAnswer(UUID answerId);

    CorrectAnswersDTO getCorrectAnswerForQuestion(UUID frageId);

    boolean timeReachedToViewReview(UUID examId);

    ReviewerDTO getReviewerById(UUID reviewerId);

    List<ExamExportDTO> getExamExportDTOs(UUID examId);

    List<ReviewExportDTO> getReviewExportDTOs(UUID examId, String studentName);
}
