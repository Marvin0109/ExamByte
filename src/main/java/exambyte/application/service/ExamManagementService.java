package exambyte.application.service;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamManagementService {

    boolean createExam(String profName, String title,
                       LocalDateTime startDate, LocalDateTime endDate, LocalDateTime resultTime);

    List<ExamDTO> getAllExams();

    boolean isExamAlreadySubmitted(UUID examFachId, String studentName);

    boolean submitExam(String studentLogin, List<UUID> frageFachIds, List<String> antwortTexte);

    ExamDTO getExam(UUID examFachId);

    List<FrageDTO> getFragenForExam(UUID examFachId);

    Optional<UUID> getProfFachIDByName(String name);

    void createFrage(FrageDTO frageDTO);

    void createChoiceFrage(FrageDTO frageDTO, KorrekteAntwortenDTO korrekteAntwortenDTO);

    String getChoiceForFrage(UUID frageFachId);

    UUID getExamByStartTime(LocalDateTime startTime);

    void deleteByFachId(UUID examFachId);

    void reset();
}
