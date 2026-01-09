package exambyte.application.service;

import exambyte.application.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExamManagementService {

    String createExam(String profName, String title,
                       LocalDateTime startDate, LocalDateTime endDate, LocalDateTime resultTime);

    List<ExamDTO> getAllExams();

    boolean isExamAlreadySubmitted(UUID examFachId, String studentName);

    boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examFachId);

    ExamDTO getExam(UUID examFachId);

    List<FrageDTO> getFragenForExam(UUID examFachId);

    Optional<UUID> getProfFachIDByName(String name);

    void createFrage(FrageDTO frageDTO);

    void createChoiceFrage(FrageDTO frageDTO, KorrekteAntwortenDTO korrekteAntwortenDTO);

    String getChoiceForFrage(UUID frageFachId);

    UUID getExamByStartTime(LocalDateTime startTime);

    void deleteByFachId(UUID examFachId);

    void reset();

    void removeOldAnswers(UUID examFachId, String name);

    VersuchDTO getSubmission(UUID examFachId, String studentLogin);

    void saveAutomaticReviewer();

    double reviewCoverage(UUID examFachId);

    List<StudentDTO> getStudentSubmittedExam(UUID examFachId);

    boolean isSubmitBeingReviewed(UUID examFachId, StudentDTO student);

    List<FrageDTO> getFreitextFragen(UUID examFachId);

    List<AntwortDTO> getFreitextAntwortenForExam(UUID examFachId);
}
