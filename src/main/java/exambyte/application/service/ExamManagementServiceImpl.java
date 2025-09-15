package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.domain.mapper.*;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.infrastructure.persistence.entities.KorrektorEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final ExamService examService;
    private final ExamRepository examRepository;
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

    public ExamManagementServiceImpl(ExamService examService, AntwortService antwortService, FrageService frageService,
                                     StudentService studentService, ProfessorService professorService,
                                     KorrektorService korrektorService,
                                     ExamRepository examRepository, ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper, AntwortDTOMapper antwortDTOMapper,
                                     KorrekteAntwortenService korrekteAntwortenService,
                                     KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper,
                                     ReviewDTOMapper reviewDTOMapper,
                                     ReviewService reviewService, AutomaticReviewService automaticReviewService) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.frageService = frageService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.korrektorService = korrektorService;
        this.examRepository = examRepository;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
        this.reviewDTOMapper = reviewDTOMapper;
        this.reviewService = reviewService;
        this.automaticReviewService = automaticReviewService;
    }

    @Override
    public boolean createExam(String professorName, String title, LocalDateTime startTime,
                              LocalDateTime endTime, LocalDateTime resultTime) {
        UUID profFachId = null;
        Optional<UUID> optionalFachID = professorService.getProfessorFachIdByName(professorName);
        if (optionalFachID.isPresent()) {
            profFachId = optionalFachID.get();
        }

        if (examRepository.findAll().size() < 12) {
            ExamDTO examDTO = new ExamDTO(null, null, title, profFachId, startTime, endTime, resultTime);
            examService.addExam(examDTOMapper.toDomain(examDTO));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examService.alleExams().stream()
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

    @Override
    public boolean submitExam(String studentLogin, Map<String, String[]> antworten, UUID examFachId) {
        try {
            UUID studentFachId = studentService.getStudentFachId(studentLogin);

            for (Map.Entry<String, String[]> entry : antworten.entrySet()) {
                try {
                    System.out.println(entry.getKey());
                    UUID frageFachId = UUID.fromString(entry.getKey());
                    Object value = entry.getValue();
                    String antwortText = "";

                    if (value instanceof String[]) {
                        antwortText = String.join(",", (String[]) value); // Mehrere Checkboxen
                    } else {
                        antwortText = value.toString(); // Single Choice oder Freitext
                    }


                    AntwortDTO antwortDTO = new AntwortDTO.AntwortDTOBuilder()
                            .fachId(null)
                            .antwortText(antwortText)
                            .frageFachId(frageFachId)
                            .studentFachId(studentFachId)
                            .antwortZeitpunkt(LocalDateTime.now())
                            .lastChangesZeitpunkt(LocalDateTime.now())
                            .build();

                    antwortService.addAntwort(antwortDTOMapper.toDomain(antwortDTO));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            //TODO: Es werden nicht zu alle MC/SC Fragen automatisch Reviews erstellt

            List<FrageDTO> fragenDTOList = frageService.getFragenForExam(examFachId).stream()
                    .map(frageDTOMapper::toDTO)
                    .toList();

            List<AntwortDTO> antwortDTOList = new ArrayList<>();

            for (FrageDTO frageDTO : fragenDTOList) {
                antwortDTOList.add(antwortDTOMapper.toDTO(
                        antwortService.findByStudentAndFrage(studentFachId, frageDTO.getFachId()))
                );
            }

            // Jetzt die MC-Liste und SC-Liste erstellen
            ReviewData reviewDataMC = new ReviewData(fragenDTOList, antwortDTOList,
                    korrekteAntwortenDTOMapper, korrekteAntwortenService);
            ReviewData reviewDataSC = new ReviewData(fragenDTOList, antwortDTOList,
                    korrekteAntwortenDTOMapper, korrekteAntwortenService);

            reviewDataMC.filterToType(QuestionTypeDTO.MC);
            reviewDataSC.filterToType(QuestionTypeDTO.SC);

            automaticReviewService.automatischeReviewMC(reviewDataMC.getFragen(), reviewDataMC.getAntworten(),
                    reviewDataMC.getKorrekteAntworten(), studentFachId);
            automaticReviewService.automatischeReviewSC(reviewDataSC.getFragen(), reviewDataSC.getAntworten(),
                    reviewDataSC.getKorrekteAntworten(), studentFachId);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ExamDTO getExam(UUID examFachId) {
        return examDTOMapper.toDTO(examRepository.findByFachId(examFachId)
                .orElseThrow(() -> new RuntimeException("Exam not found")));
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
        return examRepository.findByStartTime(startTime)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void deleteByFachId(UUID uuid) {
        examRepository.deleteByFachId(uuid);
    }

    @Override
    public void reset() {
        reviewService.deleteAll();
        antwortService.deleteAll();
        korrekteAntwortenService.deleteAll();
        frageService.deleteAll();
        examRepository.deleteAll();
    }

    @Override
    public List<VersuchDTO> getAllAttempts(UUID examFachId, String studentLogin) {
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

        // Alle Fragen des Exams und deren Maximalpunkte
        Map<UUID, FrageDTO> frageMap = frageService.getFragenForExam(examFachId).stream()
                .map(frageDTOMapper::toDTO)
                .collect(Collectors.toMap(FrageDTO::getFachId, f -> f));

        // Gesamt-MaxPunkte für MC/SC
        double gesamtMaxPunkte = frageMap.values().stream()
                .filter(f -> f.getType() == QuestionTypeDTO.MC || f.getType() == QuestionTypeDTO.SC)
                .mapToDouble(FrageDTO::getMaxPunkte)
                .sum();

        // Alle Antworten des Studenten für dieses Exam
        List<AntwortDTO> alleAntworten = frageMap.keySet().stream()
                .map(id -> antwortService.findByStudentAndFrage(studentFachId, id))
                .filter(Objects::nonNull)
                .map(antwortDTOMapper::toDTO)
                .toList();

        // Nach lastChangesZeitpunkt gruppieren
        Map<LocalDateTime, List<AntwortDTO>> gruppiert =
                alleAntworten.stream()
                        .collect(Collectors.groupingBy(
                                AntwortDTO::getLastChangesZeitpunkt,
                                TreeMap::new,
                                Collectors.toList()
                        ));

        List<VersuchDTO> result = new ArrayList<>();

        for (var entry : gruppiert.entrySet()) {
            LocalDateTime zeitpunkt = entry.getKey();
            List<AntwortDTO> antworten = entry.getValue();

            // erreichte Punkte dieses Versuchs
            double erreichte = 0.0;
            for (AntwortDTO a : antworten) {
                FrageDTO frage = frageMap.get(a.getFrageFachId());
                if (frage.getType().equals(QuestionTypeDTO.MC) || frage.getType().equals(QuestionTypeDTO.SC) &&
                !frage.getType().equals(QuestionTypeDTO.FREITEXT)) {
                    System.out.println(frage.getType().toString());
                    ReviewDTO review = reviewDTOMapper.toDTO(reviewService.getReviewByAntwortFachId(a.getFachId()));
                    if (review != null) {
                        erreichte += review.getPunkte();
                    }
                }
            }

            double prozent = gesamtMaxPunkte > 0 ? (erreichte / gesamtMaxPunkte) * 100.0 : 0.0;
            result.add(new VersuchDTO(zeitpunkt, erreichte, gesamtMaxPunkte, prozent));
        }

        return result;
    }

    @Override
    public void saveAutomaticReviewer() {
        if (korrektorService.getKorrektorByName("Automatischer Korrektor").isEmpty()) {
            korrektorService.saveKorrektor("Automatischer Korrektor");
        }
    }
}
