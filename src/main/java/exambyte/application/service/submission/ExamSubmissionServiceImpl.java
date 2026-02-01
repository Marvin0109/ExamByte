package exambyte.application.service.submission;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ReviewData;
import exambyte.application.service.question.QuestionQueryService;
import exambyte.application.service.review.AutomaticReviewService;
import exambyte.domain.mapper.*;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExamSubmissionServiceImpl implements ExamSubmissionService {

    private final ExamService examService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final FrageService frageService;
    private final ReviewService reviewService;
    private final AntwortService antwortService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final AutomaticReviewService automaticReviewService;

    private final QuestionQueryService questionQueryService;

    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;
    private final AntwortDTOMapper antwortDTOMapper;
    private final ReviewDTOMapper reviewDTOMapper;
    private final StudentDTOMapper studentDTOMapper;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    private static final Logger logger = Logger.getLogger(ExamSubmissionServiceImpl.class.getName());

    public ExamSubmissionServiceImpl(ExamService examService,
                                     ProfessorService professorService,
                                     StudentService studentService,
                                     FrageService frageService,
                                     ReviewService reviewService,
                                     AntwortService antwortService,
                                     KorrekteAntwortenService korrekteAntwortenService,
                                     AutomaticReviewService automaticReviewService,
                                     QuestionQueryService questionQueryService,
                                     ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper,
                                     AntwortDTOMapper antwortDTOMapper,
                                     ReviewDTOMapper reviewDTOMapper,
                                     StudentDTOMapper studentDTOMapper,
                                     KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper) {

        this.examService = examService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.frageService = frageService;
        this.reviewService = reviewService;
        this.antwortService = antwortService;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.automaticReviewService = automaticReviewService;
        this.questionQueryService = questionQueryService;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
        this.reviewDTOMapper = reviewDTOMapper;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
        this.studentDTOMapper = studentDTOMapper;
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

        boolean saved = saveStudentAnswers(studentFachId, antworten);
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

        List<ReviewDTO> allReviews = generateReviews(studentFachId, fragenDTOList, antwortDTOList);

        try {
            allReviews.forEach(r -> reviewService.addReview(reviewDTOMapper.toDomain(r)));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Reviews", e);
            return false;
        }

        return true;
    }

//    private boolean saveStudentAnswers(UUID studentFachId, Map<String, List<String>> antworten) {
//        try {
//            for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
//                UUID frageFachId = UUID.fromString(entry.getKey());
//                String antwortText = String.join("\n", entry.getValue());
//                AntwortDTO dto = new AntwortDTO(null, antwortText, frageFachId, studentFachId, null);
//                antwortService.addAntwort(antwortDTOMapper.toDomain(dto));
//            }
//            return true;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Fehler beim Speichern der Antworten", e);
//            return false;
//        }
//    }

//    private List<ReviewDTO> generateReviews(UUID studentFachId, List<FrageDTO> fragen, List<AntwortDTO> antworten) {
//        ReviewData mcData = new ReviewData(fragen, antworten,
//                korrekteAntwortenDTOMapper, korrekteAntwortenService);
//        ReviewData scData = new ReviewData(fragen, antworten,
//                korrekteAntwortenDTOMapper, korrekteAntwortenService);
//
//        mcData.filterToType(QuestionTypeDTO.MC);
//        scData.filterToType(QuestionTypeDTO.SC);
//
//        List<ReviewDTO> reviewsMC = automaticReviewService.automatischeReviewMC(
//                mcData.getFragen(), mcData.getAntworten(), mcData.getKorrekteAntworten(), studentFachId,
//                reviewService);
//        List<ReviewDTO> reviewsSC = automaticReviewService.automatischeReviewSC(
//                scData.getFragen(), scData.getAntworten(), scData.getKorrekteAntworten(), studentFachId,
//                reviewService);
//
//        return Stream.concat(reviewsMC.stream(), reviewsSC.stream()).toList();
//    }

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

        Map<UUID, FrageDTO> frageMap = getFragenMap(examFachId);
        List<AntwortDTO> alleAntworten = getAntworten(studentFachId, frageMap.keySet());

        // Gesamt-MaxPunkte
        double gesamtMaxPunkte = frageMap.values().stream()
                .mapToDouble(FrageDTO::maxPunkte)
                .sum();

        double erreichtePunkte = berechneErreichtePunkte(alleAntworten, frageMap);

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

    private Map<UUID, FrageDTO> getFragenMap(UUID examFachId) {
        return frageService.getFragenForExam(examFachId).stream()
                .map(frageDTOMapper::toDTO)
                .collect(Collectors.toMap(FrageDTO::fachId, f -> f));
    }

    private List<AntwortDTO> getAntworten(UUID studentFachId, Set<UUID> frageFachIds) {
        return frageFachIds.stream()
                .map(id -> antwortService.findByStudentAndFrage(studentFachId, id))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }

    private double berechneErreichtePunkte(List<AntwortDTO> antworten, Map<UUID, FrageDTO> fragen) {
        return antworten.stream()
                .mapToDouble(a -> {
                    FrageDTO f = fragen.get(a.frageFachId());
                    if (f == null) return 0;
                    Review review = reviewService.getReviewByAntwortFachId(a.fachId());
                    return review != null ? review.getPunkte() : 0;
                })
                .sum();
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examId);

        return antworten.stream()
                .collect(Collectors.toMap(
                        AntwortDTO::studentFachId,
                        a -> studentService.getStudent(a.studentFachId()),
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .map(studentDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examFachId) {
        return questionQueryService.getFreitextFragen(examFachId).stream()
                .map(frageDTO -> antwortService.findByFrageFachId(frageDTO.fachId()))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }
}
