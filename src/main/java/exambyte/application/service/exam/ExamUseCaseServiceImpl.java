package exambyte.application.service.exam;

import exambyte.application.dto.*;
import exambyte.application.service.question.QuestionQueryService;
import exambyte.application.service.review.ReviewManagementService;
import exambyte.application.service.submission.AnswerSubmissionService;
import exambyte.application.service.submission.ExamSubmissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamUseCaseServiceImpl implements ExamUseCaseService {

    private final ExamQueryService examQueryService;
    private final ProfessorQueryService professorQueryService;
    private final QuestionQueryService questionQueryService;
    private final ReviewManagementService reviewManagementService;
    private final ExamSubmissionService examSubmissionService;
    private final AnswerSubmissionService answerSubmissionService;
    private final StudentQueryService studentQueryService;


    public ExamUseCaseServiceImpl(ExamQueryService examQueryService,
                                  ProfessorQueryService professorQueryService,
                                  QuestionQueryService questionQueryService,
                                  ReviewManagementService reviewManagementService,
                                  ExamSubmissionService examSubmissionService,
                                  AnswerSubmissionService answerSubmissionService,
                                  StudentQueryService studentQueryService) {

        this.examQueryService = examQueryService;
        this.professorQueryService = professorQueryService;
        this.questionQueryService = questionQueryService;
        this.reviewManagementService = reviewManagementService;
        this.examSubmissionService = examSubmissionService;
        this.answerSubmissionService = answerSubmissionService;
        this.studentQueryService = studentQueryService;
    }

    @Override
    public String createExam(String professorName,
                              String title,
                              LocalDateTime startTime,
                              LocalDateTime endTime,
                              LocalDateTime resultTime) {

        return examSubmissionService.createExam(professorName, title, startTime, endTime, resultTime);
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examQueryService.getAllExams();
    }

    @Override
    public boolean isExamAlreadySubmitted(UUID examFachId, String studentName) {
        return examQueryService.hasStudentSubmittedExam(examFachId, studentName);
    }

    @Override
    public boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examFachId) {
        return examSubmissionService.submitExam(studentLogin, antworten, examFachId);
    }

    @Override
    public ExamDTO getExam(UUID examFachId) {
        return examQueryService.getExam(examFachId);
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examFachId) {
        return questionQueryService.getFragenForExam(examFachId);
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
        questionQueryService.createFrage(frageDTO);
    }

    @Override
    public void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices) {
        questionQueryService.createChoiceFrage(frageDTO, correctAnswer, choices);
    }

    @Override
    public String getChoiceForFrage(UUID frageFachId) {
         return questionQueryService.getChoiceForFrage(frageFachId);
    }

    @Override
    public UUID getExamByStartTime(LocalDateTime startTime) {
        return examQueryService.getExamIdByStartTime(startTime);
    }

    @Override
    public void deleteById(UUID uuid) {
        examQueryService.deleteByFachId(uuid);
    }

    @Override
    public void reset() {
        examQueryService.resetAllExamDataCascade();
    }

    @Override
    public void removeOldAnswers(UUID examFachId, String name) {
        examSubmissionService.removeOldAnswers(examFachId, name);
    }

    @Override
    public VersuchDTO getSubmission(UUID examFachId, String studentLogin) {
        return examSubmissionService.getSubmission(examFachId, studentLogin);
    }

    @Override
    public void saveAutomaticReviewer() {
        reviewManagementService.saveAutomaticReviewer();
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
        return questionQueryService.getFreitextFragen(examFachId);
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examFachId) {
        return answerSubmissionService.getFreitextAntwortenForExam(examFachId);
    }

    @Override
    public boolean antwortHasReview(AntwortDTO antwort) {
       return reviewManagementService.antwortHasReview(antwort.fachId());
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortFachId, UUID korrektorFachId) {
        reviewManagementService.createReview(bewertung, punkte, antwortFachId, korrektorFachId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return reviewManagementService.getReviewerIdByName(name);
    }
}
