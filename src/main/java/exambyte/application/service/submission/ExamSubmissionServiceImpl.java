package exambyte.application.service.submission;

import exambyte.application.dto.*;
import exambyte.application.service.exam.ExamQueryService;
import exambyte.application.service.exam.ProfessorQueryService;
import exambyte.application.service.exam.StudentQueryService;
import exambyte.application.service.question.QuestionQueryService;
import exambyte.application.service.review.ReviewGenerationService;
import exambyte.application.service.review.ReviewManagementService;
import exambyte.application.service.review.ReviewManagementServiceImpl;
import exambyte.application.service.review.ScoringService;
import exambyte.domain.entitymapper.ReviewMapper;
import exambyte.domain.mapper.*;
import exambyte.domain.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExamSubmissionServiceImpl implements ExamSubmissionService {

    private final AnswerSubmissionService answerSubmissionService;
    private final ReviewGenerationService reviewGenerationService;
    private final ReviewManagementService reviewManagementService;
    private final QuestionQueryService questionQueryService;
    private final ScoringService scoringService;
    private final ProfessorQueryService professorQueryService;
    private final StudentQueryService studentQueryService;
    private final ExamQueryService examQueryService;

    private static final Logger logger = Logger.getLogger(ExamSubmissionServiceImpl.class.getName());

    public ExamSubmissionServiceImpl(AnswerSubmissionService answerSubmissionService,
                                     ReviewGenerationService reviewGenerationService,
                                     QuestionQueryService questionQueryService,
                                     ScoringService scoringService,
                                     ProfessorQueryService professorQueryService,
                                     StudentQueryService studentQueryService,
                                     ExamQueryService examQueryService,
                                     ReviewManagementService reviewManagementService) {

        this.answerSubmissionService = answerSubmissionService;
        this.reviewGenerationService = reviewGenerationService;
        this.questionQueryService = questionQueryService;
        this.scoringService = scoringService;
        this.professorQueryService = professorQueryService;
        this.studentQueryService = studentQueryService;
        this.examQueryService = examQueryService;
        this.reviewManagementService = reviewManagementService;
    }

    @Override
    public String createExam(String profName,
                             String title,
                             LocalDateTime start,
                             LocalDateTime end,
                             LocalDateTime result) {

        UUID profFachId = professorQueryService.getProfIdByName(profName)
                .orElseThrow(() -> new IllegalStateException("Professor noch nicht gespeichert: " + profName));

        if (start.isAfter(end) || start.isEqual(end)) {
            return "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!";
        }

        if (result.isBefore(end) || result.isEqual(end)) {
            return "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!";
        }

        List<ExamDTO> exams = examQueryService.getAllExams();
        int examCount = exams.size();

        if (examCount >= 12) {
            return "Die maximale Kapazität von 12 Exams ist nun überschritten worden!";
        }

        boolean startTimeExists = exams.stream()
                .anyMatch(e -> e.startTime().truncatedTo(ChronoUnit.MINUTES)
                        .equals(start.truncatedTo(ChronoUnit.MINUTES)));

        if (startTimeExists) {
            return "Ein Exam mit der selben Startzeit ist schon vorhanden!";
        }

        ExamDTO examDTO = new ExamDTO(null, title, profFachId, start, end, result);
        examQueryService.addExam(examDTO);
        return "";
    }

    @Override
    public boolean submitExam(String studentName, Map<String, List<String>> antworten, UUID examId) {
        UUID studentFachId;
        try {
            studentFachId = studentQueryService.getStudentIdByName(studentName);
        } catch (Exception e) {
            String msg = "Student nicht gefunden: " + studentName;
            logger.log(Level.SEVERE, msg, e);
            return false;
        }

        boolean saved = answerSubmissionService.saveAnswers(studentFachId, antworten);
        if (!saved) {
            return false;
        }

        List<FrageDTO> fragenDTOList = questionQueryService.getFragenForExam(examId);

        List<AntwortDTO> antwortDTOList = fragenDTOList.stream()
                .map(f -> answerSubmissionService.findByStudentAndFrage(studentFachId, f.fachId()))
                .filter(Objects::nonNull)
                .toList();

        List<ReviewDTO> allReviews = reviewGenerationService.generateReviews(
                studentFachId,
                fragenDTOList,
                antwortDTOList);

        try {
            allReviews.forEach(r -> reviewManagementService.createReview(
                    r.bewertung(),
                    r.punkte(),
                    r.antwortFachId(),
                    r.korrektorFachId()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Reviews", e);
            return false;
        }

        return true;
    }

    @Override
    public void removeOldAnswers(UUID examId, String name) {
        UUID studentFachID = studentQueryService.getStudentIdByName(name);

        List<FrageDTO> fragenDTOList = questionQueryService.getFragenForExam(examId);

        List<UUID> antwortenToDelete = new ArrayList<>();
        for (FrageDTO frageDTO : fragenDTOList) {
            antwortenToDelete.add(
                    answerSubmissionService.findByStudentAndFrage(
                            studentFachID, frageDTO.fachId())
                            .fachId());
        }

        List<UUID> reviewsToDelete = new ArrayList<>();
        for (UUID id : antwortenToDelete) {
            if (reviewManagementService.antwortHasReview(id)) {
                reviewsToDelete.add(reviewManagementService.getReviewIdByAntwortId(id));
            }
        }

        for (UUID id : antwortenToDelete) {
            answerSubmissionService.deleteAntwort(id);
        }

        for (UUID id : reviewsToDelete) {
            reviewManagementService.deleteReview(id);
        }
    }

    @Override
    public VersuchDTO getSubmission(UUID examFachId, String studentName) {
        UUID studentFachId = studentQueryService.getStudentIdByName(studentName);

        Map<UUID, FrageDTO> frageMap = questionQueryService.getFragenUUIDMap(examFachId);
        List<AntwortDTO> alleAntworten = answerSubmissionService.getAntworten(studentFachId, frageMap.keySet());

        // Gesamt-MaxPunkte
        double gesamtMaxPunkte = frageMap.values().stream()
                .mapToDouble(FrageDTO::maxPunkte)
                .sum();

        double erreichtePunkte = scoringService.berechneErreichtePunkte(alleAntworten, frageMap);

        double prozent = gesamtMaxPunkte > 0
                ? (erreichtePunkte / gesamtMaxPunkte) * 100.0
                : 0.0;

        LocalDateTime zeitpunkt = alleAntworten.stream()
                .map(AntwortDTO::antwortZeitpunkt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        return new VersuchDTO(
                zeitpunkt,
                erreichtePunkte,
                gesamtMaxPunkte,
                prozent
        );
    }
}
