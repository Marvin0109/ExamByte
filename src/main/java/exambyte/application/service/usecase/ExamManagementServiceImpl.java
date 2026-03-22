package exambyte.application.service.usecase;

import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.service.review.ReviewGenerationService;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final AnswerQueryService answerQueryService;
    private final ReviewGenerationService reviewGenerationService;
    private final QuestionQueryService questionQueryService;
    private final ScoringService scoringService;
    private final ProfessorQueryService professorQueryService;
    private final StudentQueryService studentQueryService;
    private final ExamQueryService examQueryService;
    private final ReviewQueryService reviewQueryService;
    private final Clock clock;

    private static final int EXAM_COUNT = 12;

    private static final Logger logger = Logger.getLogger(ExamManagementServiceImpl.class.getName());

    public ExamManagementServiceImpl(AnswerQueryService answerQueryService,
                                     ReviewGenerationService reviewGenerationService,
                                     QuestionQueryService questionQueryService,
                                     ScoringService scoringService,
                                     ProfessorQueryService professorQueryService,
                                     StudentQueryService studentQueryService,
                                     ExamQueryService examQueryService,
                                     ReviewQueryService reviewQueryService,
                                     Clock clock) {

        this.answerQueryService = answerQueryService;
        this.reviewGenerationService = reviewGenerationService;
        this.questionQueryService = questionQueryService;
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
                .anyMatch(e -> e.start().truncatedTo(ChronoUnit.MINUTES)
                        .equals(start.truncatedTo(ChronoUnit.MINUTES)));

        if (startTimeExists) {
            return "Ein Exam mit der selben Startzeit ist schon vorhanden!";
        }

        ExamDTO examDTO = new ExamDTO(null, title, profId, start, end, result);
        examQueryService.addExam(examDTO);
        return "";
    }

    @Transactional(rollbackFor = {Exception.class, NichtVorhandenException.class})
    @Override
    public SubmitExamResult submitExam(String studentName, Map<String, List<String>> answerMap, UUID examId) {
        UUID studentId = resolveStudent(studentName);
        if (studentId == null) return SubmitExamResult.STUDENT_NOT_FOUND;

        answerQueryService.saveAnswers(studentId, answerMap);

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
        List<QuestionDTO> questions = questionQueryService.getQuestionsForExam(examId);

        List<AnswerDTO> answerList = questions.stream()
                .map(f -> answerQueryService.findByStudentAndFrage(studentId, f.id()))
                .filter(Objects::nonNull)
                .toList();

        List<ReviewDTO> allReviews = reviewGenerationService.generateReviews(
                studentId,
                questions,
                answerList);

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
                reviewDTO.text(),
                reviewDTO.points(),
                reviewDTO.answerId(),
                reviewDTO.reviewerId()
        );
    }

    @Override
    public AttemptDTO getSubmission(UUID examId, String studentName) {
        UUID studentId = studentQueryService.getStudentIdByName(studentName);

        ExamDTO exam = examQueryService.getExam(examId);
        Map<UUID, QuestionDTO> questionMap = questionQueryService.getQuestionUUIDMap(examId);
        List<AnswerDTO> allAnswers = answerQueryService.getAnswers(studentId, questionMap.keySet());

        // Gesamt-MaxPunkte
        double gesamtMaxPunkte = questionMap.values().stream()
                .mapToDouble(QuestionDTO::points)
                .sum();

        double erreichtePunkte = scoringService.berechneErreichtePunkte(allAnswers, questionMap, exam.result());

        double prozent = gesamtMaxPunkte > 0
                ? (erreichtePunkte / gesamtMaxPunkte) * 100.0
                : 0.0;

        LocalDateTime zeitpunkt = allAnswers.stream()
                .map(AnswerDTO::submitTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        return new AttemptDTO(
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
    public UUID getExamIdByStartTime(LocalDateTime start) {
        return examQueryService.getExamIdByStartTime(start);
    }

    @Override
    public boolean deleteById(UUID id) {
        ExamDTO exam = examQueryService.getExam(id);

        if (now().isBefore(exam.start()) || exam.result().isBefore(now())) {
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
                if (exam.end().isAfter(now())) {
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
        return exam.result().isBefore(now().truncatedTo(ChronoUnit.MINUTES));
    }
}
