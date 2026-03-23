package exambyte.application.service.usecase;

import exambyte.application.dto.*;
import exambyte.application.service.query.*;
import exambyte.application.service.review.ReviewGenerationService;
import exambyte.application.exception.NotFoundException;
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

    private final AnswerService answerService;
    private final ReviewGenerationService reviewGenerationService;
    private final QuestionService questionService;
    private final ScoringService scoringService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final ExamService examService;
    private final ReviewService reviewService;
    private final Clock clock;

    private static final int EXAM_COUNT = 12;

    private static final Logger logger = Logger.getLogger(ExamManagementServiceImpl.class.getName());

    public ExamManagementServiceImpl(AnswerService answerService,
                                     ReviewGenerationService reviewGenerationService,
                                     QuestionService questionService,
                                     ScoringService scoringService,
                                     ProfessorService professorService,
                                     StudentService studentService,
                                     ExamService examService,
                                     ReviewService reviewService,
                                     Clock clock) {

        this.answerService = answerService;
        this.reviewGenerationService = reviewGenerationService;
        this.questionService = questionService;
        this.scoringService = scoringService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.examService = examService;
        this.reviewService = reviewService;
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

        UUID profId = professorService.getProfIdByName(profName)
                .orElseThrow(() -> new IllegalStateException("Professor not saved yet: " + profName));

        if (start.isAfter(end) || start.isEqual(end)) {
            return "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!";
        }

        if (result.isBefore(end) || result.isEqual(end)) {
            return "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!";
        }

        List<ExamDTO> exams = examService.getAllExams();
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
        examService.addExam(examDTO);
        return "";
    }

    @Transactional(rollbackFor = {Exception.class, NotFoundException.class})
    @Override
    public SubmitExamResult submitExam(String studentName, Map<String, List<String>> answerMap, UUID examId) {
        UUID studentId = resolveStudent(studentName);
        if (studentId == null) return SubmitExamResult.STUDENT_NOT_FOUND;

        ExamDTO exam = examService.getExam(examId);
        if (checkSubmitTime(exam.end())) {
            return SubmitExamResult.SAVE_ANSWERS_FAILED;
        }

        answerService.saveAnswers(studentId, answerMap);

        return generateAndSaveReviews(studentId, examId);
    }

    private boolean checkSubmitTime(LocalDateTime end) {
        return end.isBefore(now()) || end.isEqual(now());
    }

    private UUID resolveStudent(String studentName) {
        try {
            return studentService.getStudentIdByName(studentName);
        } catch (Exception e) {
            String msg = "Student not found: " + studentName;
            logger.log(Level.SEVERE, msg, e);
            return null;
        }
    }

    private SubmitExamResult generateAndSaveReviews(UUID studentId, UUID examId) {
        List<QuestionDTO> questions = questionService.getQuestionsForExam(examId);

        List<AnswerDTO> answerList = questions.stream()
                .map(f -> answerService.findByStudentAndQuestion(studentId, f.id()))
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
            logger.log(Level.SEVERE, "Failed saving reviews", e);
            return SubmitExamResult.REVIEW_SAVE_FAILED;
        }
    }

    private void saveReviews(ReviewDTO reviewDTO) {
        reviewService.createReview(
                reviewDTO.text(),
                reviewDTO.points(),
                reviewDTO.answerId(),
                reviewDTO.reviewerId()
        );
    }

    @Override
    public AttemptDTO getSubmission(UUID examId, String studentName) {
        UUID studentId = studentService.getStudentIdByName(studentName);

        ExamDTO exam = examService.getExam(examId);
        Map<UUID, QuestionDTO> questionMap = questionService.getQuestionUUIDMap(examId);
        List<AnswerDTO> allAnswers = answerService.getAnswers(studentId, questionMap.keySet());

        double totalPoints = questionMap.values().stream()
                .mapToDouble(QuestionDTO::points)
                .sum();

        double accumulatedPoints = scoringService.accumulatedPoints(allAnswers, questionMap, exam.result());

        double scoreInPercent = totalPoints > 0
                ? (accumulatedPoints / totalPoints) * 100.0
                : 0.0;

        LocalDateTime submitTime = allAnswers.stream()
                .map(AnswerDTO::submitTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        return new AttemptDTO(
                submitTime,
                accumulatedPoints,
                totalPoints,
                scoreInPercent
        );
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examService.getAllExams();
    }

    @Override
    public boolean hasStudentSubmittedExam(UUID examId, String studentName) {
        return examService.hasStudentSubmittedExam(examId, studentName);
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examService.getExam(examId);
    }

    @Override
    public UUID getExamIdByStartTime(LocalDateTime start) {
        return examService.getExamIdByStartTime(start);
    }

    @Override
    public boolean deleteById(UUID id) {
        ExamDTO exam = examService.getExam(id);

        if (now().isBefore(exam.start()) || exam.result().isBefore(now())) {
            examService.deleteById(exam.id());
            return true;
        }
        return false;
    }

    @Override
    public boolean resetAllExamDataCascade() {
        List<ExamDTO> examList = examService.getAllExams();

        if (examList.size() != EXAM_COUNT) return false;
        else {
            for (ExamDTO exam : examList) {
                if (exam.end().isAfter(now())) {
                    return false;
                }
            }
            examService.resetAllExamDataCascade();
        }

        return true;
    }

    @Override
    public boolean allowedToViewReview(UUID examId) {
        ExamDTO exam = examService.getExam(examId);
        return exam.result().isBefore(now().truncatedTo(ChronoUnit.MINUTES));
    }
}
