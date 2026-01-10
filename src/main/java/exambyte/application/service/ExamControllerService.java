package exambyte.application.service;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.web.form.ExamForm;
import exambyte.web.form.ExamTimeInfo;
import exambyte.web.form.ReviewCoverageForm;
import exambyte.web.form.SubmitInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    UUID getProfUUID(String name);

    void createQuestions(ExamForm form, UUID profFachID, UUID examUUID);

    VersuchDTO getAttempt(UUID examUUID, String studentLogin);

    List<ReviewCoverageForm> getReviewCoverage(List<ExamDTO> examDTOList);

    ExamTimeInfo getExamTimeInfo(ExamDTO examDTO);

    void removeOldAnswersAndReviews(UUID examUUID, String name);

    boolean submitExam(String name, Map<String, List<String>> answers, UUID examUUiD);

    List<SubmitInfo> getSubmitInfo(UUID examUUID);
}
