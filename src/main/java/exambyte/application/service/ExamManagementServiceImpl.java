package exambyte.application.service;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.AntwortDTOMapper;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import exambyte.infrastructure.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final ExamService examService;
    private final ExamRepository examRepository;
    private final AntwortService antwortService;
    private final FrageService frageService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;
    private final AntwortDTOMapper antwortDTOMapper;

    public ExamManagementServiceImpl(ExamService examService, AntwortService antwortService, FrageService frageService,
                                     StudentService studentService, ProfessorService professorService,
                                     ExamRepository examRepository, ExamDTOMapper examDTOMapper,
                                     FrageDTOMapper frageDTOMapper, AntwortDTOMapper antwortDTOMapper) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.frageService = frageService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.examRepository = examRepository;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
        this.antwortDTOMapper = antwortDTOMapper;
    }

    @Override
    public boolean createExam(String professorName, String title, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        UUID profFachId = null;
        Optional<UUID> optionalFachID = professorService.getProfessorFachId(professorName);
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
    public boolean submitExam(String studentLogin, List<UUID> frageFachIds, List<String> antwortTexte) {
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

        if (frageFachIds.size() != antwortTexte.size()) {
            return false;
        }

        for (int i = 0; i < frageFachIds.size(); i++) {
            UUID frageFachId = frageFachIds.get(i);
            String antwortText = antwortTexte.get(i);

            if (antwortService.findByStudentAndFrage(studentFachId, frageFachId) != null) {
                return false;
            }

            AntwortDTO antwortDTO = new AntwortDTO.AntwortDTOBuilder()
                    .antwortText(antwortText)
                    .frageFachId(frageFachId)
                    .studentFachId(studentFachId)
                    .antwortZeitpunkt(LocalDateTime.now())
                    .lastChangesZeitpunkt(LocalDateTime.now())
                    .build();

            antwortService.addAntwort(antwortDTOMapper.toDomain(antwortDTO));
        }

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
        return Optional.of(professorService.getProfessorFachId(name)
                .orElseThrow(NichtVorhandenException::new));
    }
}
