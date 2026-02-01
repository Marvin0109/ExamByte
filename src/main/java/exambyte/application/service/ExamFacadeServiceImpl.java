package exambyte.application.service;

import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.service.usecase.ReviewManagementService;
import exambyte.application.service.usecase.ExamManagementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamFacadeServiceImpl implements ExamFacadeService {

    private final ReviewManagementService reviewManagementService;
    private final ExamManagementService examManagementService;
    private final FrageQueryService frageQueryService;
    private final ProfessorQueryService professorQueryService;
    private final KorrektorQueryService korrektorQueryService;
    private final StudentQueryService studentQueryService;
    private final AntwortQueryService antwortQueryService;
    private final ReviewQueryService reviewQueryService;


    public ExamFacadeServiceImpl(ReviewManagementService reviewManagementService,
                                 ExamManagementService examManagementService,
                                 FrageQueryService frageQueryService,
                                 ProfessorQueryService professorQueryService,
                                 KorrektorQueryService korrektorQueryService,
                                 StudentQueryService studentQueryService,
                                 AntwortQueryService antwortQueryService,
                                 ReviewQueryService reviewQueryService) {

        this.reviewManagementService = reviewManagementService;
        this.examManagementService = examManagementService;
        this.frageQueryService = frageQueryService;
        this.professorQueryService = professorQueryService;
        this.korrektorQueryService = korrektorQueryService;
        this.studentQueryService = studentQueryService;
        this.antwortQueryService = antwortQueryService;
        this.reviewQueryService = reviewQueryService;
    }

    @Override
    public String createExam(String professorName,
                              String title,
                              LocalDateTime startTime,
                              LocalDateTime endTime,
                              LocalDateTime resultTime) {

        return examManagementService.createExam(professorName, title, startTime, endTime, resultTime);
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examManagementService.getAllExams();
    }

    @Override
    public boolean isExamAlreadySubmitted(UUID examFachId, String studentName) {
        return examManagementService.hasStudentSubmittedExam(examFachId, studentName);
    }

    @Override
    public boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examFachId) {
        return examManagementService.submitExam(studentLogin, antworten, examFachId);
    }

    @Override
    public ExamDTO getExam(UUID examFachId) {
        return examManagementService.getExam(examFachId);
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examFachId) {
        return frageQueryService.getFragenForExam(examFachId);
    }

    @Override
    public Optional<UUID> getProfIDByName(String name) {
        return professorQueryService.getProfIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessor(UUID profFachId) {
        return professorQueryService.getProfessorById(profFachId);
    }

    @Override
    public void createFrage(FrageDTO frageDTO) {
        frageQueryService.createFrage(frageDTO);
    }

    @Override
    public void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices) {
        frageQueryService.createChoiceFrage(frageDTO, correctAnswer, choices);
    }

    @Override
    public String getChoiceForFrage(UUID frageFachId) {
         return frageQueryService.getChoiceForFrage(frageFachId);
    }

    @Override
    public UUID getExamByStartTime(LocalDateTime startTime) {
        return examManagementService.getExamIdByStartTime(startTime);
    }

    @Override
    public void deleteById(UUID uuid) {
        examManagementService.deleteById(uuid);
    }

    @Override
    public void reset() {
        examManagementService.resetAllExamDataCascade();
    }

    @Override
    public void removeOldAnswers(UUID examFachId, String name) {
        examManagementService.removeOldAnswers(examFachId, name);
    }

    @Override
    public VersuchDTO getSubmission(UUID examFachId, String studentLogin) {
        return examManagementService.getSubmission(examFachId, studentLogin);
    }

    @Override
    public void saveAutomaticReviewer() {
        korrektorQueryService.saveAutomaticReviewer();
    }

    @Override
    public double reviewCoverage(UUID examFachId) {
        return reviewManagementService.getReviewCoverage(examFachId);
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examFachId) {
        return studentQueryService.getStudentSubmittedExam(examFachId);
    }

    @Override
    public boolean isSubmitBeingReviewed(UUID examFachId, UUID studentId) {
        return reviewManagementService.submitHasReview(examFachId, studentId);
    }

    @Override
    public List<FrageDTO> getFreitextFragen(UUID examFachId) {
        return frageQueryService.getFreitextFragen(examFachId);
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examFachId) {
        return antwortQueryService.getFreitextAntwortenForExam(examFachId);
    }

    @Override
    public boolean antwortHasReview(AntwortDTO antwort) {
       return reviewQueryService.antwortHasReview(antwort.fachId());
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortFachId, UUID korrektorFachId) {
        reviewQueryService.createReview(bewertung, punkte, antwortFachId, korrektorFachId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return korrektorQueryService.getReviewerIdByName(name);
    }
}
