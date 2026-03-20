package exambyte.application.service.query;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.mapper.FrageDTOMapper;
import exambyte.domain.service.AnswerService;
import exambyte.domain.service.ExamService;
import exambyte.domain.service.FrageService;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ExamQueryServiceImpl implements ExamQueryService {

    private final ExamService examService;
    private final StudentService studentService;
    private final FrageService frageService;
    private final AnswerService answerService;

    private final ExamDTOMapper examDTOMapper;
    private final FrageDTOMapper frageDTOMapper;

    public ExamQueryServiceImpl(ExamService examService,
                                StudentService studentService,
                                FrageService frageService,
                                AnswerService answerService,
                                ExamDTOMapper examDTOMapper,
                                FrageDTOMapper frageDTOMapper) {
        this.examService = examService;
        this.studentService = studentService;
        this.frageService = frageService;
        this.answerService = answerService;
        this.examDTOMapper = examDTOMapper;
        this.frageDTOMapper = frageDTOMapper;
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examDTOMapper.toDTO(examService.getExam(examId));
    }

    @Override
    public UUID getExamIdByStartTime(LocalDateTime start) {
        List<ExamDTO> examList = examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .toList();

        for (ExamDTO examDTO : examList) {
            if (start.truncatedTo(ChronoUnit.MINUTES)
                    .equals(examDTO.startTime().truncatedTo(ChronoUnit.MINUTES))) {
                return examDTO.id();
            }
        }

        return null;
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .sorted(Comparator.comparing(ExamDTO::startTime))
                .toList();
    }

    @Override
    public boolean hasStudentSubmittedExam(UUID examId, String studentName) {
        UUID studentId = studentService.getStudentId(studentName);
        List<FrageDTO> fragen = frageDTOMapper.toFrageDTOList(frageService.getFragenForExam(examId));

        return fragen.stream()
                .anyMatch(frage ->
                        answerService.findByStudentAndFrage(studentId, frage.id()) != null);
    }

    @Override
    public void deleteById(UUID examId) {
        examService.deleteById(examId);
    }

    @Transactional
    @Override
    public void resetAllExamDataCascade() {
        examService.deleteAll();
    }

    @Override
    public void addExam(ExamDTO examDTO) {
        examService.addExam(examDTOMapper.toDomain(examDTO));
    }
}
