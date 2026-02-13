package exambyte.application.service;

import exambyte.application.dto.*;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.create_review.ReviewForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamControllerService {

    ExamForm createExamForm();

    ExamForm fillExamForm(UUID examUUID);

    UUID getExamUUIDByStartTime(LocalDateTime startTime);

    String createExam(ExamForm form, String name);

    ExamDTO getExamByUUID(UUID examUUID);

    List<ExamDTO> getAllExams();

    List<FrageDTO> getFragenForExam(UUID examUUID);

    boolean examIsAlreadySubmitted(UUID examUUID, String studentLogin);

    void createQuestions(ExamForm form, UUID profFachID, UUID examUUID);

    VersuchDTO getAttempt(UUID examUUID, String studentLogin);

    List<ReviewCoverageForm> getReviewCoverage(List<ExamDTO> examDTOList);

    ExamTimeInfo getExamTimeInfo(ExamDTO examDTO);

    void removeOldAnswersAndReviews(UUID examUUID, String name);

    boolean submitExam(String name, Map<String, List<String>> answers, UUID examUUiD);

    List<SubmitInfo> getSubmitInfo(UUID examUUID);

    void saveAutomaticReviewer();

    Optional<UUID> getProfFachIDByName(String name);

    boolean reset();

    boolean deleteExam(UUID examUUID);

    ProfessorDTO getProfessorByFachId(UUID fachId);

    double getZulassungsProgress(String studentName);

    boolean hasAnyFailedAttempt(String studentName);

    Map<FrageDTO, AntwortDTO> getFreitextAntwortenForExamAndStudent(UUID examUUID, UUID studentUUID);

    List<AnswerForm> createAnswerForm(Map<FrageDTO, AntwortDTO> map);

    void createReview(ReviewForm reviewForm, UUID antwortFachId, UUID korrektorFachId);

    UUID getReviewerByName(String name);

    ReviewViewForm prepareReviewViewForm(UUID examUUID, String studentName);

    boolean checkTimeForReviewView(UUID examId);

    OldDataForm fillOldDataForm(UUID examId, String studentName);

    SubmitForm fillSubmitFormWithData(OldDataForm form);
}
