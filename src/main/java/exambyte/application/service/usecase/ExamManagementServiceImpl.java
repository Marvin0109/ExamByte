package exambyte.application.service.usecase;

import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.service.review.ReviewGenerationService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final AntwortQueryService antwortQueryService;
    private final ReviewGenerationService reviewGenerationService;
    private final FrageQueryService frageQueryService;
    private final ScoringService scoringService;
    private final ProfessorQueryService professorQueryService;
    private final StudentQueryService studentQueryService;
    private final ExamQueryService examQueryService;
    private final ReviewQueryService reviewQueryService;
    private final Clock clock;

    private static final int EXAM_COUNT = 12;

    private static final Logger logger = Logger.getLogger(ExamManagementServiceImpl.class.getName());

    public ExamManagementServiceImpl(AntwortQueryService antwortQueryService,
                                     ReviewGenerationService reviewGenerationService,
                                     FrageQueryService frageQueryService,
                                     ScoringService scoringService,
                                     ProfessorQueryService professorQueryService,
                                     StudentQueryService studentQueryService,
                                     ExamQueryService examQueryService,
                                     ReviewQueryService reviewQueryService,
                                     Clock clock) {

        this.antwortQueryService = antwortQueryService;
        this.reviewGenerationService = reviewGenerationService;
        this.frageQueryService = frageQueryService;
        this.scoringService = scoringService;
        this.professorQueryService = professorQueryService;
        this.studentQueryService = studentQueryService;
        this.examQueryService = examQueryService;
        this.reviewQueryService = reviewQueryService;
        this.clock = clock;
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public String createExam(String profName,
                             String title,
                             LocalDateTime start,
                             LocalDateTime end,
                             LocalDateTime result) {

        UUID profId = professorQueryService.getProfIdByName(profName)
                .orElseThrow(() -> new IllegalStateException("Professor noch nicht gespeichert: " + profName));

        if (start.isAfter(end) || start.isEqual(end)) {
            return "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!";
        }

        if (result.isBefore(end) || result.isEqual(end)) {
            return "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!";
        }

        List<ExamDTO> exams = examQueryService.getAllExams();
        int examCount = exams.size();

        if (examCount >= EXAM_COUNT) {
            return "Die maximale Kapazität von 12 Exams ist nun überschritten worden!";
        }

        boolean startTimeExists = exams.stream()
                .anyMatch(e -> e.startTime().truncatedTo(ChronoUnit.MINUTES)
                        .equals(start.truncatedTo(ChronoUnit.MINUTES)));

        if (startTimeExists) {
            return "Ein Exam mit der selben Startzeit ist schon vorhanden!";
        }

        ExamDTO examDTO = new ExamDTO(null, title, profId, start, end, result);
        examQueryService.addExam(examDTO);
        return "";
    }

    @Override
    public SubmitExamResult submitExam(String studentName, Map<String, List<String>> antworten, UUID examId) {
        UUID studentId = resolveStudent(studentName);
        if (studentId == null) return SubmitExamResult.STUDENT_NOT_FOUND;

        if(!antwortQueryService.saveAnswers(studentId, antworten)) {
            return SubmitExamResult.SAVE_ANSWERS_FAILED;
        }

        return generateAndSaveReviews(studentId, examId);
    }

    private UUID resolveStudent(String studentName) {
        try {
            return studentQueryService.getStudentIdByName(studentName);
        } catch (Exception e) {
            String msg = "Student nicht gefunden: " + studentName;
            logger.log(Level.SEVERE, msg, e);
            return null;
        }
    }

    private SubmitExamResult generateAndSaveReviews(UUID studentId, UUID examId) {
        List<FrageDTO> fragenList = frageQueryService.getFragenForExam(examId);

        List<AntwortDTO> antwortList = fragenList.stream()
                .map(f -> antwortQueryService.findByStudentAndFrage(studentId, f.id()))
                .filter(Objects::nonNull)
                .toList();

        List<ReviewDTO> allReviews = reviewGenerationService.generateReviews(
                studentId,
                fragenList,
                antwortList);

        try {
            allReviews.forEach(this::saveReviews);
            return SubmitExamResult.SUCCESS;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Reviews", e);
            return SubmitExamResult.REVIEW_SAVE_FAILED;
        }
    }

    private void saveReviews(ReviewDTO reviewDTO) {
        reviewQueryService.createReview(
                reviewDTO.bewertung(),
                reviewDTO.punkte(),
                reviewDTO.antwortId(),
                reviewDTO.korrektorId()
        );
    }

    @Override
    public void removeOldAnswers(UUID examId, String name) {
        UUID studentID = studentQueryService.getStudentIdByName(name);

        List<FrageDTO> fragenDTOList = frageQueryService.getFragenForExam(examId);

        List<UUID> antwortenToDelete = new ArrayList<>();
        for (FrageDTO frageDTO : fragenDTOList) {
            antwortenToDelete.add(
                    antwortQueryService.findByStudentAndFrage(
                            studentID, frageDTO.id())
                            .id());
        }

        List<UUID> reviewsToDelete = new ArrayList<>();
        for (UUID id : antwortenToDelete) {
            if (reviewQueryService.antwortHasReview(id)) {
                reviewsToDelete.add(reviewQueryService.getReviewIdByAntwortId(id));
            }
        }

        for (UUID id : antwortenToDelete) {
            antwortQueryService.deleteAntwort(id);
        }

        for (UUID id : reviewsToDelete) {
            reviewQueryService.deleteReview(id);
        }
    }

    @Override
    public VersuchDTO getSubmission(UUID examId, String studentName) {
        UUID studentId = studentQueryService.getStudentIdByName(studentName);

        ExamDTO exam = examQueryService.getExam(examId);
        Map<UUID, FrageDTO> frageMap = frageQueryService.getFragenUUIDMap(examId);
        List<AntwortDTO> alleAntworten = antwortQueryService.getAntworten(studentId, frageMap.keySet());

        // Gesamt-MaxPunkte
        double gesamtMaxPunkte = frageMap.values().stream()
                .mapToDouble(FrageDTO::maxPunkte)
                .sum();

        double erreichtePunkte = scoringService.berechneErreichtePunkte(alleAntworten, frageMap, exam.resultTime());

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

    @Override
    public List<ExamDTO> getAllExams() {
        return examQueryService.getAllExams();
    }

    @Override
    public boolean hasStudentSubmittedExam(UUID examId, String studentName) {
        return examQueryService.hasStudentSubmittedExam(examId, studentName);
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examQueryService.getExam(examId);
    }

    @Override
    public UUID getExamIdByStartTime(LocalDateTime startTime) {
        return examQueryService.getExamIdByStartTime(startTime);
    }

    @Override
    public boolean deleteById(UUID id) {
        ExamDTO exam = examQueryService.getExam(id);

        if (now().isBefore(exam.startTime()) || exam.resultTime().isBefore(now())) {
            examQueryService.deleteById(exam.id());
            return true;
        }
        return false;
    }

    @Override
    public boolean resetAllExamDataCascade() {
        List<ExamDTO> examList = examQueryService.getAllExams();

        if (examList.size() != EXAM_COUNT) return false;
        else {
            for (ExamDTO exam : examList) {
                if (exam.endTime().isAfter(now())) {
                    return false;
                }
            }
            examQueryService.resetAllExamDataCascade();
        }

        return true;
    }

    @Override
    public boolean allowedToViewReview(UUID examId) {
        ExamDTO exam = examQueryService.getExam(examId);
        return exam.resultTime().isBefore(now().truncatedTo(ChronoUnit.MINUTES));
    }
}
