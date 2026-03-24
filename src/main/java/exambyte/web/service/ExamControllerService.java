package exambyte.web.service;

import exambyte.application.dto.*;
import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.dto.export.ReviewExportDTO;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.create_review.ReviewForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamControllerService {

    ExamForm createExamForm(int countQuestions);

    ExamForm fillExamForm(UUID examId);

    UUID getExamUUIDByStartTime(LocalDateTime startTime);

    String createExam(ExamForm form, String name);

    ExamDTO getExamByUUID(UUID examId);

    List<ExamDTO> getAllExams();

    List<QuestionDTO> getQuestionsForExam(UUID examId);

    boolean examIsAlreadySubmitted(UUID examId, String studentLogin);

    void createQuestions(ExamForm form, UUID examId);

    AttemptDTO getAttempt(UUID examId, String studentLogin);

    List<ReviewCoverageForm> getReviewCoverage(List<ExamDTO> examDTOList);

    ExamTimeInfo getExamTimeInfo(ExamDTO examDTO);

    boolean submitExam(String name, Map<String, List<String>> answers, UUID examId);

    List<SubmitInfo> getSubmitInfo(UUID examId);

    void saveAutomaticReviewer();

    Optional<UUID> getProfIdByName(String name);

    boolean reset();

    boolean deleteExam(UUID examId);

    ProfessorDTO getProfessorById(UUID id);

    double getEligibilityProgress(String studentName);

    boolean hasAnyFailedAttempt(String studentName);

    Map<QuestionDTO, AnswerDTO> getFreeResponseSolutionForExamAndStudent(UUID examId, UUID studentId);

    List<AnswerForm> createAnswerForm(Map<QuestionDTO, AnswerDTO> map);

    void createReview(ReviewForm reviewForm, UUID answerId, UUID reviewerId);

    UUID getReviewerByName(String name);

    ReviewViewForm prepareReviewViewForm(UUID examId, String studentName);

    boolean checkTimeForReviewView(UUID examId);

    OldDataForm fillOldDataForm(UUID examId, String studentName);

    SubmitForm fillSubmitFormWithData(OldDataForm form);

    List<ExamExportDTO> getExamExport(UUID examId);

    List<ReviewExportDTO> getReviewExport(UUID examId, String studentName);

    List<QuestionTypeWeb> createQuestionTypeList(int mcCount, int scCount, int freeResponseCount);

    ExamViewForm getExamView(UUID examId);
}
