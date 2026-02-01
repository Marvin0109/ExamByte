package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.domain.mapper.*;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.model.aggregate.user.Professor;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.logging.Logger;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final ExamService examService;
    private final AntwortService antwortService;
    private final FrageService frageService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final KorrektorService korrektorService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final ReviewService reviewService;
    private final AutomaticReviewService automaticReviewService;

    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;
    private final AntwortDTOMapper antwortDTOMapper;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;
    private final ReviewDTOMapper reviewDTOMapper;
    private final StudentDTOMapper studentDTOMapper;
    private final ProfessorDTOMapper professorDTOMapper;

    private static final Logger logger = Logger.getLogger(ExamManagementServiceImpl.class.getName());

    @Autowired
    public ExamManagementServiceImpl(ExamService examService,
                                     AntwortService antwortService,
                                     FrageService frageService,
                                     StudentService studentService,
                                     ProfessorService professorService,
                                     KorrektorService korrektorService,
                                     ReviewService reviewService,
                                     AutomaticReviewService automaticReviewService,
                                     KorrekteAntwortenService korrekteAntwortenService,
                                     ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper,
                                     AntwortDTOMapper antwortDTOMapper,
                                     KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper,
                                     ReviewDTOMapper reviewDTOMapper,
                                     StudentDTOMapper studentDTOMapper,
                                     ProfessorDTOMapper professorDTOMapper) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.frageService = frageService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.korrektorService = korrektorService;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.reviewService = reviewService;
        this.automaticReviewService = automaticReviewService;

        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
        this.professorDTOMapper = professorDTOMapper;
        this.reviewDTOMapper = reviewDTOMapper;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public String createExam(String professorName,
                              String title,
                              LocalDateTime startTime,
                              LocalDateTime endTime,
                              LocalDateTime resultTime) {

        UUID profFachId = professorService.getProfessorFachIdByName(professorName)
                .orElseThrow(() -> new IllegalStateException("Professor noch nicht gespeichert: " + professorName));

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            return "Start-Zeitpunkt muss vor End-Zeitpunkt liegen!";
        }

        if (resultTime.isBefore(endTime) || resultTime.isEqual(endTime)) {
            return "Ergebnis-Zeitpunkt muss nach End-Zeitpunkt liegen!";
        }

        int examCount = examService.allExams().size();

        if (examCount >= 12) {
            return "Die maximale Kapazität von 12 Exams ist nun überschritten worden!";
        }

        boolean startTimeExists = examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .anyMatch(e -> e.startTime().truncatedTo(ChronoUnit.MINUTES)
                        .equals(startTime.truncatedTo(ChronoUnit.MINUTES)));

        if (startTimeExists) {
            return "Ein Exam mit der selben Startzeit ist schon vorhanden!";
        }

        ExamDTO examDTO = new ExamDTO(null, title, profFachId, startTime, endTime, resultTime);
        examService.addExam(examDTOMapper.toDomain(examDTO));
        return "";
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .sorted(Comparator.comparing(ExamDTO::startTime))
                .toList();
    }

    @Override
    public boolean isExamAlreadySubmitted(UUID examFachId, String studentName) {
        UUID studentFachId = studentService.getStudentFachId(studentName);
        List<FrageDTO> fragen = frageDTOMapper.toFrageDTOList(frageService.getFragenForExam(examFachId));

        return fragen.stream()
                .anyMatch(frage ->
                        antwortService.findByStudentAndFrage(studentFachId, frage.fachId()) != null);
    }

    /**
     * Speichert die Antworten eines Studenten zu einem Exam und erstellt automatische
     * Reviews für Single-Choice- und Multiple-Choice-Fragen.
     *
     * @param studentLogin Login-Name des Studenten
     * @param antworten    Map<FrageFachId, Antworten[]> – alle vom Frontend gesendeten Antworten
     * @param examFachId   Fach-ID des Exams
     * @return true, wenn alle Antworten und Reviews erfolgreich gespeichert wurden
     */
    @Override
    public boolean submitExam(String studentLogin, Map<String, List<String>> antworten, UUID examFachId) {
        UUID studentFachId;
        try {
            studentFachId = studentService.getStudentFachId(studentLogin);
        } catch (Exception e) {
            String msg = "Student nicht gefunden: " + studentLogin;
            logger.log(Level.SEVERE, msg, e);
            return false;
        }

        boolean saved = saveStudentAnswers(studentFachId, antworten);
        if (!saved) {
            return false;
        }

        List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examFachId).stream()
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

    private boolean saveStudentAnswers(UUID studentFachId, Map<String, List<String>> antworten) {
        try {
            for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
                UUID frageFachId = UUID.fromString(entry.getKey());
                String antwortText = String.join("\n", entry.getValue());
                AntwortDTO dto = new AntwortDTO(null, antwortText, frageFachId, studentFachId, null);
                antwortService.addAntwort(antwortDTOMapper.toDomain(dto));
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Antworten", e);
            return false;
        }
    }

    private List<ReviewDTO> generateReviews(UUID studentFachId, List<FrageDTO> fragen, List<AntwortDTO> antworten) {
        ReviewData mcData = new ReviewData(fragen, antworten,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);
        ReviewData scData = new ReviewData(fragen, antworten,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);

        mcData.filterToType(QuestionTypeDTO.MC);
        scData.filterToType(QuestionTypeDTO.SC);

        List<ReviewDTO> reviewsMC = automaticReviewService.automatischeReviewMC(
                mcData.getFragen(), mcData.getAntworten(), mcData.getKorrekteAntworten(), studentFachId,
                reviewService);
        List<ReviewDTO> reviewsSC = automaticReviewService.automatischeReviewSC(
                scData.getFragen(), scData.getAntworten(), scData.getKorrekteAntworten(), studentFachId,
                reviewService);

        return Stream.concat(reviewsMC.stream(), reviewsSC.stream()).toList();
    }

    @Override
    public ExamDTO getExam(UUID examFachId) {
        return examDTOMapper.toDTO(examService.getExam(examFachId));
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examFachId) {
        return frageDTOMapper.toFrageDTOList(frageService.getFragenForExam(examFachId));
    }

    @Override
    public Optional<UUID> getProfFachIDByName(String name) {
        return professorService.getProfessorFachIdByName(name);
    }

    @Override
    public ProfessorDTO getProfessor(UUID profFachId) {
        Professor professor = professorService.getProfessor(profFachId);
        return professorDTOMapper.toDTO(professor);
    }

    @Override
    public void createFrage(FrageDTO frageDTO) {
        frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
    }

    @Override
    public void createChoiceFrage(FrageDTO frageDTO, String correctAnswer, String choices) {
        UUID frageFachId = frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
        KorrekteAntwortenDTO dto = new KorrekteAntwortenDTO(null, correctAnswer, choices, frageFachId);
        korrekteAntwortenService.addKorrekteAntwort(korrekteAntwortenDTOMapper.toDomain(dto));
    }

    @Override
    public String getChoiceForFrage(UUID frageFachId) {
         return korrekteAntwortenService.findKorrekteAntwort(frageFachId).getAntwortOptionen();
    }

    @Override
    public UUID getExamByStartTime(LocalDateTime startTime) {
        List<ExamDTO> examList = examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .toList();

        for (ExamDTO examDTO : examList) {
            if (startTime.truncatedTo(ChronoUnit.MINUTES)
                    .equals(examDTO.startTime().truncatedTo(ChronoUnit.MINUTES))) {
                return examDTO.fachId();
            }
        }

        return null;
    }

    @Override
    public void deleteByFachId(UUID uuid) {
        examService.deleteByFachId(uuid);
    }

    @Override
    public void reset() {
        // Entweder examService.deleteAll() oder
        // diese Variante, mehr boilerplate, aber robuster
        reviewService.deleteAll();
        antwortService.deleteAll();
        korrekteAntwortenService.deleteAll();
        frageService.deleteAll();
        examService.deleteAll();
    }

    @Override
    public void removeOldAnswers(UUID examFachId, String name) {
        UUID studentFachID = studentService.getStudentFachId(name);

        List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examFachId).stream()
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
    public VersuchDTO getSubmission(UUID examFachId, String studentLogin) {
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

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
    public void saveAutomaticReviewer() {
        if (korrektorService.getKorrektorByName("Automatischer Korrektor").isEmpty()) {
            korrektorService.saveKorrektor("Automatischer Korrektor");
        }
    }

    @Override
    public double reviewCoverage(UUID examFachId) {
        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examFachId);

        List<ReviewDTO> reviewsTotal = new ArrayList<>();

        for (AntwortDTO antwortDTO : antworten) {
            Review review = reviewService.getReviewByAntwortFachId(antwortDTO.fachId());
            if (review != null) {
                reviewsTotal.add(reviewDTOMapper.toDTO(review));
            }
        }

        double coverage = antworten.isEmpty()
                ? 0.0
                : (double) reviewsTotal.size() / antworten.size() * 100;

        return Math.round(coverage * 100.0) / 100.0;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examFachId) {

        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examFachId);

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
    public boolean isSubmitBeingReviewed(UUID examFachId, StudentDTO student) {
        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examFachId);

        List<UUID> studentAntwortList = antworten.stream()
                .filter(a -> a.studentFachId().equals(student.fachId()))
                .map(AntwortDTO::fachId)
                .toList();

        for (UUID uuid : studentAntwortList) {
            if (reviewService.getReviewByAntwortFachId(uuid) == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<FrageDTO> getFreitextFragen(UUID examFachId) {
        List<Frage> fragen = frageService.getFragenForExam(examFachId);

        return fragen.stream()
                .filter(frage -> QuestionType.FREITEXT == frage.getType())
                .map(frageDTOMapper::toDTO)
                .toList();
    }

    @Override
    public List<AntwortDTO> getFreitextAntwortenForExam(UUID examFachId) {
        return getFreitextFragen(examFachId).stream()
                .map(frageDTO -> antwortService.findByFrageFachId(frageDTO.fachId()))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }

    @Override
    public boolean antwortHasReview(AntwortDTO antwort) {
        return reviewService.getReviewByAntwortFachId(antwort.fachId()) != null;
    }

    @Override
    public void createReview(String bewertung, int punkte, UUID antwortFachId, UUID korrektorFachId) {
        Review review = new Review.ReviewBuilder()
                .korrektorFachId(korrektorFachId)
                .punkte(punkte)
                .antwortFachId(antwortFachId)
                .bewertung(bewertung)
                .build();

        reviewService.addReview(review);
    }

    @Override
    public UUID getReviewerByName(String name) {
        Optional<Korrektor> k = korrektorService.getKorrektorByName(name);
        return k.map(Korrektor::uuid).orElse(null);
    }
}
