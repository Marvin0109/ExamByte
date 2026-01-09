package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.domain.mapper.*;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                                     StudentDTOMapper studentDTOMapper) {
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
        this.reviewDTOMapper = reviewDTOMapper;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public String createExam(String professorName,
                              String title,
                              LocalDateTime startTime,
                              LocalDateTime endTime,
                              LocalDateTime resultTime) {

        UUID profFachId = null;
        Optional<UUID> optionalFachID = professorService.getProfessorFachIdByName(professorName);
        if (optionalFachID.isPresent()) {
            profFachId = optionalFachID.get();
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

        ExamDTO examDTO = new ExamDTO(null, null, title, profFachId, startTime, endTime, resultTime);
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
                .anyMatch(frage -> antwortService.findByStudentAndFrage(studentFachId, frage.getFachId()) != null);
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
        // 1. Student ermitteln
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

        // 2. Eingehende Antworten des Studenten speichern
        for (Map.Entry<String, List<String>> entry : antworten.entrySet()) {
            String frageKey = entry.getKey();
            List<String> value = entry.getValue();

            try {
                // Key der Map muss eine UUID (Frage-Fach-ID) sein
                UUID frageFachId = UUID.fromString(frageKey);

                String antwortText = String.join("\n", value);

                AntwortDTO dto = new AntwortDTO.AntwortDTOBuilder()
                        .fachId(null)
                        .frageFachId(frageFachId)
                        .studentFachId(studentFachId)
                        .antwortText(antwortText)
                        .antwortZeitpunkt(LocalDateTime.now())
                        .lastChangesZeitpunkt(LocalDateTime.now())
                        .build();

                antwortService.addAntwort(antwortDTOMapper.toDomain(dto));

            } catch (IllegalArgumentException ex) {
                // Falls ein Key keine gültige UUID ist → Eingabe ignorieren, weiter mit nächster Frage
                System.out.println("WARNUNG: Ungültige FrageFachId in SubmitExam: "
                        + frageKey + " (" + ex.getMessage() + ")");
            }
        }

        // 3. Alle Fragen dieses Exams abrufen
        List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examFachId).stream()
                .map(frageDTOMapper::toDTO)
                .toList();

        // 4. Alle gespeicherten Antworten des Studenten für dieses Exam abrufen
        List<AntwortDTO> antwortDTOList = fragenDTOList.stream()
                .map(f -> antwortDTOMapper.toDTO(
                        antwortService.findByStudentAndFrage(studentFachId, f.getFachId())))
                .filter(Objects::nonNull)
                .toList();

        // 5. ReviewData-Objekte für MC und SC vorbereiten
        ReviewData mcData = new ReviewData(fragenDTOList, antwortDTOList,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);
        ReviewData scData = new ReviewData(fragenDTOList, antwortDTOList,
                korrekteAntwortenDTOMapper, korrekteAntwortenService);

        mcData.filterToType(QuestionTypeDTO.MC);
        scData.filterToType(QuestionTypeDTO.SC);

        // 6. Automatische Reviews erzeugen
        List<ReviewDTO> reviewsMC = automaticReviewService.automatischeReviewMC(
                mcData.getFragen(), mcData.getAntworten(), mcData.getKorrekteAntworten(), studentFachId,
                reviewService);
        List<ReviewDTO> reviewsSC = automaticReviewService.automatischeReviewSC(
                scData.getFragen(), scData.getAntworten(), scData.getKorrekteAntworten(), studentFachId,
                reviewService);

        // 7. Reviews speichern
        Stream.concat(reviewsMC.stream(), reviewsSC.stream())
                .forEach(r -> reviewService.addReview(reviewDTOMapper.toDomain(r)));

        return true;
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
    public void createFrage(FrageDTO frageDTO) {
        frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
    }

    @Override
    public void createChoiceFrage(FrageDTO frageDTO, KorrekteAntwortenDTO korrekteAntwortenDTO) {
        UUID frageFachID = frageService.addFrage(frageDTOMapper.toDomain(frageDTO));
        korrekteAntwortenDTO.setFrageFachID(frageFachID);
        korrekteAntwortenService.addKorrekteAntwort(korrekteAntwortenDTOMapper.toDomain(korrekteAntwortenDTO));
    }

    @Override
    public String getChoiceForFrage(UUID frageFachId) {
         return korrekteAntwortenService.findKorrekteAntwort(frageFachId).getAntwort_optionen();
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
                        antwortService.findByStudentAndFrage(studentFachID, frageDTO.getFachId())
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

        // Alle Fragen des Exams und deren Maximalpunkte
        Map<UUID, FrageDTO> frageMap = frageService.getFragenForExam(examFachId).stream()
                .map(frageDTOMapper::toDTO)
                .collect(Collectors.toMap(FrageDTO::getFachId, f -> f));

        // Gesamt-MaxPunkte
        double gesamtMaxPunkte = frageMap.values().stream()
                .mapToDouble(FrageDTO::getMaxPunkte)
                .sum();

        // Alle Antworten des Studenten für dieses Exam
        List<AntwortDTO> alleAntworten = frageMap.keySet().stream()
                .map(id -> antwortService.findByStudentAndFrage(studentFachId, id))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();

        double erreichtePunkte = 0.0;

        for (AntwortDTO antwortDTO : alleAntworten) {
            FrageDTO frage = frageMap.get(antwortDTO.getFrageFachId());
            if (frage == null) {
                continue;
            }

            Review review = reviewService.getReviewByAntwortFachId(antwortDTO.getFachId());
            if (review != null) {
                erreichtePunkte += review.getPunkte();
            }
        }

        double prozent = gesamtMaxPunkte > 0
                ? (erreichtePunkte / gesamtMaxPunkte) * 100.0
                : 0.0;

        LocalDateTime zeitpunkt = alleAntworten.stream()
                .map(AntwortDTO::getLastChangesZeitpunkt)
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
            Review review = reviewService.getReviewByAntwortFachId(antwortDTO.getFachId());
            if (review != null) {
                reviewsTotal.add(reviewDTOMapper.toDTO(review));
            }
        }

        System.out.println(reviewsTotal.size());
        System.out.println(antworten.size());

        double coverage = antworten.isEmpty()
                ? 0.0
                : (double) reviewsTotal.size() / antworten.size() * 100;

        return Math.round(coverage * 100.0) / 100.0;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examFachId) {

        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examFachId);

        return antworten.stream()
                .map(AntwortDTO::getStudentFachId)
                .map(studentService::getStudent)
                .map(studentDTOMapper::toDTO)
                .toList();
    }

    @Override
    public boolean isSubmitBeingReviewed(UUID examFachId, StudentDTO student) {
        List<AntwortDTO> antworten = getFreitextAntwortenForExam(examFachId);

        List<UUID> studentAntwortList = antworten.stream()
                .filter(a -> a.getStudentFachId().equals(student.fachId()))
                .map(AntwortDTO::getFachId)
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
                .map(frageDTO -> antwortService.findByFrageFachId(frageDTO.getFachId()))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();
    }
}
