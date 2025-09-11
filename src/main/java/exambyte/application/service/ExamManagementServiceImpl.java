package exambyte.application.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.mapper.KorrekteAntwortenDTOMapper;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import exambyte.infrastructure.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final ExamService examService;
    private final ExamRepository examRepository;
    private final AntwortService antwortService;
    private final FrageService frageService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final KorrekteAntwortenService korrekteAntwortenService;
    private final ReviewService reviewService;
    private final AutomaticReviewService automaticReviewService;
    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;
    private final AntwortDTOMapper antwortDTOMapper;
    private final KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper;

    public ExamManagementServiceImpl(ExamService examService, AntwortService antwortService, FrageService frageService,
                                     StudentService studentService, ProfessorService professorService,
                                     ExamRepository examRepository, ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper, AntwortDTOMapper antwortDTOMapper,
                                     KorrekteAntwortenService korrekteAntwortenService, KorrekteAntwortenDTOMapper korrekteAntwortenDTOMapper,
                                     ReviewService reviewService, AutomaticReviewService automaticReviewService) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.frageService = frageService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.examRepository = examRepository;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
        this.korrekteAntwortenService = korrekteAntwortenService;
        this.korrekteAntwortenDTOMapper = korrekteAntwortenDTOMapper;
        this.reviewService = reviewService;
        this.automaticReviewService = automaticReviewService;
    }

    @Override
    public boolean createExam(String professorName, String title, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
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
        return examDTOMapper.toExamDTOList(examService.alleExams());
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
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

        for (Map.Entry<String, String[]> entry : antworten.entrySet()) {
            try {
                UUID frageFachId = UUID.fromString(entry.getKey());
                String antwortText = String.join(" ", entry.getValue());

                AntwortDTO antwortDTO = new AntwortDTO.AntwortDTOBuilder()
                        .antwortText(antwortText)
                        .frageFachId(frageFachId)
                        .studentFachId(studentFachId)
                        .antwortZeitpunkt(LocalDateTime.now())
                        .lastChangesZeitpunkt(LocalDateTime.now())
                        .build();

                antwortService.addAntwort(antwortDTOMapper.toDomain(antwortDTO));
            } catch (IllegalArgumentException e) {
                // Key war keine UUID, z.B. CSRF oder andere hidden fields → einfach ignorieren
                continue;
            }
        }

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
}
