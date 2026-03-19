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
                       LocalDateTime startDate, LocalDateTime endDate, LocalDateTime resultTime);

    List<ExamDTO> getAllExams();

    boolean isExamAlreadySubmitted(UUID examId, String studentName);

    boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examId);

    ExamDTO getExam(UUID examId);

    List<FrageDTO> getFragenForExam(UUID examId);

    Optional<UUID> getProfIDByName(String name);

    ProfessorDTO getProfessor(UUID profId);

    void createFrage(FrageDTO frageDTO);

    void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices);

    String getChoiceForFrage(UUID frageId);

    UUID getExamByStartTime(LocalDateTime startTime);

    boolean deleteById(UUID examId);

    boolean reset();

    VersuchDTO getSubmission(UUID examId, String studentLogin);

    void saveAutomaticReviewer();

    double reviewCoverage(UUID examId);

    List<StudentDTO> getStudentSubmittedExam(UUID examId);

    boolean isSubmitBeingReviewed(UUID examId, UUID studentId);

    List<FrageDTO> getFreeResponseFragen(UUID examId);

    List<AntwortDTO> getFreeResponseAntwortenForExam(UUID examId);

    boolean antwortHasReview(AntwortDTO antwort);

    void createReview(String bewertung, double punkte, UUID antwortId, UUID korrektorId);

    UUID getReviewerByName(String name);

    UUID getStudentIdByName(String name);

    AntwortDTO getAntwortForFrageAndStudent(UUID frageId, UUID studentId);

    ReviewDTO getReviewForAntwort(UUID antwortId);

    KorrekteAntwortenDTO getLoesungForFrage(UUID frageId);

    boolean timeReachedToViewReview(UUID examId);

    KorrektorDTO getReviewerById(UUID reviewerId);

    List<ExamExportDTO> getExamExportDTOs(UUID examId);

    List<ReviewExportDTO> getReviewExportDTOs(UUID examId, String studentName);
}
