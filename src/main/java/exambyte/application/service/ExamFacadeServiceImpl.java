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
    private final ReviewerQueryService reviewerQueryService;
    private final StudentQueryService studentQueryService;
    private final AnswerQueryService answerQueryService;
    private final ReviewQueryService reviewQueryService;
    private final CorrectAnswersQueryService correctAnswersQueryService;


    public ExamFacadeServiceImpl(ReviewManagementService reviewManagementService,
                                 ExamManagementService examManagementService,
                                 ExamExportService examExportService,
                                 ReviewExportService reviewExportService,
                                 FrageQueryService frageQueryService,
                                 ProfessorQueryService professorQueryService,
                                 ReviewerQueryService reviewerQueryService,
                                 StudentQueryService studentQueryService,
                                 AnswerQueryService answerQueryService,
                                 ReviewQueryService reviewQueryService,
                                 CorrectAnswersQueryService correctAnswersQueryService) {

        this.reviewManagementService = reviewManagementService;
        this.examManagementService = examManagementService;
        this.examExportService = examExportService;
        this.reviewExportService = reviewExportService;
        this.frageQueryService = frageQueryService;
        this.professorQueryService = professorQueryService;
        this.reviewerQueryService = reviewerQueryService;
        this.studentQueryService = studentQueryService;
        this.answerQueryService = answerQueryService;
        this.reviewQueryService = reviewQueryService;
        this.correctAnswersQueryService = correctAnswersQueryService;
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
    public boolean submitExam(String studentLogin, Map<String, List<String>> answer, UUID examId) {
        SubmitExamResult result = examManagementService.submitExam(studentLogin, answer, examId);
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
    public AttemptDTO getSubmission(UUID examId, String studentLogin) {
        return examManagementService.getSubmission(examId, studentLogin);
    }

    @Override
    public void saveAutomaticReviewer() {
        reviewerQueryService.saveAutomaticReviewer();
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
    public List<FrageDTO> getFreeResponseFragen(UUID examId) {
        return frageQueryService.getFreeResponseFragen(examId);
    }

    @Override
    public List<AnswerDTO> getFreeResponseSolutionForExam(UUID examId) {
        return answerQueryService.getFreeResponseAnswersForExam(examId);
    }

    @Override
    public boolean answerHasReview(AnswerDTO answer) {
       return reviewQueryService.answerHasReview(answer.id());
    }

    @Override
    public void createReview(String bewertung, double punkte, UUID answerId, UUID reviewerId) {
        reviewQueryService.createReview(bewertung, punkte, answerId, reviewerId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return reviewerQueryService.getReviewerIdByName(name);
    }

    @Override
    public UUID getStudentIdByName(String name) {
        return studentQueryService.getStudentIdByName(name);
    }

    @Override
    public AnswerDTO getAnswerForFrageAndStudent(UUID frageId, UUID studentId) {
        return answerQueryService.findByStudentAndFrage(studentId, frageId);
    }

    @Override
    public ReviewDTO getReviewForAnswer(UUID answerId) {
        return reviewQueryService.getReviewByAnswerId(answerId);
    }

    @Override
    public CorrectAnswersDTO getLoesungForFrage(UUID frageId) {
        return correctAnswersQueryService.getSolutionForFrage(frageId);
    }

    @Override
    public boolean timeReachedToViewReview(UUID examId) {
        return examManagementService.allowedToViewReview(examId);
    }

    @Override
    public ReviewerDTO getReviewerById(UUID reviewerId) {
        return reviewerQueryService.getReviewerById(reviewerId);
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
