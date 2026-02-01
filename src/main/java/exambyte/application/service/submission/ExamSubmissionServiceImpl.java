package exambyte.application.service.submission;

import exambyte.application.dto.*;
import exambyte.application.service.question.QuestionQueryService;
import exambyte.application.service.review.ReviewGenerationService;
import exambyte.application.service.review.ScoringService;
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
    private final QuestionQueryService questionQueryService;
    private final ScoringService scoringService;

    private final ExamService examService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final FrageService frageService;
    private final ReviewService reviewService;
    private final AntwortService antwortService;

    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;
    private final AntwortDTOMapper antwortDTOMapper;
    private final ReviewDTOMapper reviewDTOMapper;

    private static final Logger logger = Logger.getLogger(ExamSubmissionServiceImpl.class.getName());

    public ExamSubmissionServiceImpl(AnswerSubmissionService answerSubmissionService,
                                     ReviewGenerationService reviewGenerationService,
                                     QuestionQueryService questionQueryService,
                                     ScoringService scoringService,
                                     ExamService examService,
                                     ProfessorService professorService,
                                     StudentService studentService,
                                     FrageService frageService,
                                     ReviewService reviewService,
                                     AntwortService antwortService,
                                     ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper,
                                     AntwortDTOMapper antwortDTOMapper,
                                     ReviewDTOMapper reviewDTOMapper) {

        this.answerSubmissionService = answerSubmissionService;
        this.reviewGenerationService = reviewGenerationService;
        this.questionQueryService = questionQueryService;
        this.scoringService = scoringService;
        this.examService = examService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.frageService = frageService;
        this.reviewService = reviewService;
        this.antwortService = antwortService;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
        this.reviewDTOMapper = reviewDTOMapper;
    }

    @Override
    public String createExam(String profName,
                             String title,
                             LocalDateTime start,
                             LocalDateTime end,
                             LocalDateTime result) {

        UUID profFachId = professorService.getProfessorFachIdByName(profName)
                .orElseThrow(() -> new IllegalStateException("Professor noch nicht gespeichert: " + profName));

        if (start.isAfter(end) || start.isEqual(end)) {
            return "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!";
        }

        if (result.isBefore(end) || result.isEqual(end)) {
            return "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!";
        }

        int examCount = examService.allExams().size();

        if (examCount >= 12) {
            return "Die maximale Kapazität von 12 Exams ist nun überschritten worden!";
        }

        boolean startTimeExists = examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .anyMatch(e -> e.startTime().truncatedTo(ChronoUnit.MINUTES)
                        .equals(start.truncatedTo(ChronoUnit.MINUTES)));

        if (startTimeExists) {
            return "Ein Exam mit der selben Startzeit ist schon vorhanden!";
        }

        ExamDTO examDTO = new ExamDTO(null, title, profFachId, start, end, result);
        examService.addExam(examDTOMapper.toDomain(examDTO));
        return "";
    }

    @Override
    public boolean submitExam(String studentName, Map<String, List<String>> antworten, UUID examId) {
        UUID studentFachId;
        try {
            studentFachId = studentService.getStudentFachId(studentName);
        } catch (Exception e) {
            String msg = "Student nicht gefunden: " + studentName;
            logger.log(Level.SEVERE, msg, e);
            return false;
        }

        boolean saved = answerSubmissionService.saveAnswers(studentFachId, antworten);
        if (!saved) {
            return false;
        }

        List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examId).stream()
                .map(frageDTOMapper::toDTO)
                .toList();

        List<AntwortDTO> antwortDTOList = fragenDTOList.stream()
                .map(f -> antwortDTOMapper.toDTO(
                        antwortService.findByStudentAndFrage(studentFachId, f.fachId())))
                .filter(Objects::nonNull)
                .toList();

        List<ReviewDTO> allReviews = reviewGenerationService.generateReviews(
                studentFachId,
                fragenDTOList,
                antwortDTOList);

        try {
            allReviews.forEach(r -> reviewService.addReview(reviewDTOMapper.toDomain(r)));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Reviews", e);
            return false;
        }

        return true;
    }

    @Override
    public void removeOldAnswers(UUID examId, String name) {
        UUID studentFachID = studentService.getStudentFachId(name);

        List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examId).stream()
                .map(frageDTOMapper::toDTO)
                .toList();

        List<UUID> antwortenToDelete = new ArrayList<>();
        for (FrageDTO frageDTO : fragenDTOList) {
            antwortenToDelete.add(
                    antwortService.findByStudentAndFrage(studentFachID, frageDTO.fachId())
                            .getFachId());
        }

        List<UUID> reviewsToDelete = new ArrayList<>();
        for (UUID id : antwortenToDelete) {
            if (reviewService.getReviewByAntwortFachId(id) != null) {
                reviewsToDelete.add(reviewService.getReviewByAntwortFachId(id).getFachId());
            }
        }

        for (UUID id : antwortenToDelete) {
            antwortService.deleteAnswer(id);
        }

        for (UUID id : reviewsToDelete) {
            reviewService.deleteReview(id);
        }
    }

    @Override
    public VersuchDTO getSubmission(UUID examFachId, String studentName) {
        UUID studentFachId = studentService.getStudentFachId(studentName);

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
