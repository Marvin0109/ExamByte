package exambyte.application.service;

import exambyte.application.dto.*;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
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
    private final FrageQueryService frageQueryService;
    private final ProfessorQueryService professorQueryService;
    private final KorrektorQueryService korrektorQueryService;
    private final StudentQueryService studentQueryService;
    private final AntwortQueryService antwortQueryService;
    private final ReviewQueryService reviewQueryService;
    private final KorrekteAntwortenQueryService korrekteAntwortenQueryService;


    public ExamFacadeServiceImpl(ReviewManagementService reviewManagementService,
                                 ExamManagementService examManagementService,
                                 ExamExportService examExportService,
                                 ReviewExportService reviewExportService,
                                 FrageQueryService frageQueryService,
                                 ProfessorQueryService professorQueryService,
                                 KorrektorQueryService korrektorQueryService,
                                 StudentQueryService studentQueryService,
                                 AntwortQueryService antwortQueryService,
                                 ReviewQueryService reviewQueryService,
                                 KorrekteAntwortenQueryService korrekteAntwortenQueryService) {

        this.reviewManagementService = reviewManagementService;
        this.examManagementService = examManagementService;
        this.examExportService = examExportService;
        this.reviewExportService = reviewExportService;
        this.frageQueryService = frageQueryService;
        this.professorQueryService = professorQueryService;
        this.korrektorQueryService = korrektorQueryService;
        this.studentQueryService = studentQueryService;
        this.antwortQueryService = antwortQueryService;
        this.reviewQueryService = reviewQueryService;
        this.korrekteAntwortenQueryService = korrekteAntwortenQueryService;
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
    public boolean isExamAlreadySubmitted(UUID examId, String studentName) {
        return examManagementService.hasStudentSubmittedExam(examId, studentName);
    }

    @Override
    public boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examId) {
        SubmitExamResult result = examManagementService.submitExam(studentLogin, antworten, examId);
        return result.equals(SubmitExamResult.SUCCESS);
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examManagementService.getExam(examId);
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examId) {
        return frageQueryService.getFragenForExam(examId);
    }

    @Override
    public Optional<UUID> getProfIDByName(String name) {
        return professorQueryService.getProfIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessor(UUID profId) {
        return professorQueryService.getProfessorById(profId);
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
    public String getChoiceForFrage(UUID frageId) {
         return frageQueryService.getChoiceForFrage(frageId);
    }

    @Override
    public UUID getExamByStartTime(LocalDateTime startTime) {
        return examManagementService.getExamIdByStartTime(startTime);
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return examManagementService.deleteById(uuid);
    }

    @Override
    public boolean reset() {
        return examManagementService.resetAllExamDataCascade();
    }

    @Override
    public VersuchDTO getSubmission(UUID examId, String studentLogin) {
        return examManagementService.getSubmission(examId, studentLogin);
    }

    @Override
    public void saveAutomaticReviewer() {
        korrektorQueryService.saveAutomaticReviewer();
    }

    @Override
    public double reviewCoverage(UUID examId) {
        return reviewManagementService.getReviewCoverage(examId);
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        return studentQueryService.getStudentSubmittedExam(examId);
    }

    @Override
    public boolean isSubmitBeingReviewed(UUID examId, UUID studentId) {
        return reviewManagementService.submitHasReview(examId, studentId);
    }

    @Override
    public List<FrageDTO> getFreitextFragen(UUID examId) {
        return frageQueryService.getFreitextFragen(examId);
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examId) {
        return antwortQueryService.getFreitextAntwortenForExam(examId);
    }

    @Override
    public boolean antwortHasReview(AntwortDTO antwort) {
       return reviewQueryService.antwortHasReview(antwort.id());
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortId, UUID korrektorId) {
        reviewQueryService.createReview(bewertung, punkte, antwortId, korrektorId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return korrektorQueryService.getReviewerIdByName(name);
    }

    @Override
    public UUID getStudentIdByName(String name) {
        return studentQueryService.getStudentIdByName(name);
    }

    @Override
    public AntwortDTO getAntwortForFrageAndStudent(UUID frageId, UUID studentId) {
        return antwortQueryService.findByStudentAndFrage(studentId, frageId);
    }

    @Override
    public ReviewDTO getReviewForAntwort(UUID antwortId) {
        return reviewQueryService.getReviewByAntwortId(antwortId);
    }

    @Override
    public KorrekteAntwortenDTO getLoesungForFrage(UUID frageId) {
        return korrekteAntwortenQueryService.getLoesungForFrage(frageId);
    }

    @Override
    public boolean timeReachedToViewReview(UUID examId) {
        return examManagementService.allowedToViewReview(examId);
    }

    @Override
    public KorrektorDTO getReviewerById(UUID reviewerId) {
        return korrektorQueryService.getReviewerById(reviewerId);
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
