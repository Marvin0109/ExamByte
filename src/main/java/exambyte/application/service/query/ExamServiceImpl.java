package exambyte.application.service.query;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.ExamDTOMapper;
import exambyte.domain.model.exam.Exam;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repository;
    private final StudentService studentService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    private final ExamDTOMapper examDTOMapper;

    public ExamServiceImpl(ExamRepository repository,
                           StudentService studentService,
                           QuestionService questionService,
                           AnswerService answerService,
                           ExamDTOMapper examDTOMapper) {
        this.repository = repository;
        this.studentService = studentService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.examDTOMapper = examDTOMapper;
    }

    @Override
    public ExamDTO getExam(UUID examId) {
        return examDTOMapper.toDTO(findById(examId));
    }

    @Override
    public UUID getExamIdByStartTime(LocalDateTime start) {
        List<ExamDTO> examList = findAll().stream()
                .map(examDTOMapper::toDTO)
                .toList();

        for (ExamDTO examDTO : examList) {
            if (start.truncatedTo(ChronoUnit.MINUTES)
                    .equals(examDTO.start().truncatedTo(ChronoUnit.MINUTES))) {
                return examDTO.id();
            }
        }

        return null;
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return findAll().stream()
                .map(examDTOMapper::toDTO)
                .sorted(Comparator.comparing(ExamDTO::start))
                .toList();
    }

    @Override
    public boolean hasStudentSubmittedExam(UUID examId, String studentName) {
        UUID studentId = studentService.getStudentIdByName(studentName);
        List<QuestionDTO> questions = questionService.getQuestionsForExam(examId);

        return questions.stream()
                .anyMatch(question ->
                        answerService.findByStudentAndQuestion(studentId, question.id()) != null);
    }

    @Override
    public void deleteById(UUID examId) {
        repository.deleteById(examId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    @Override
    public void resetAllExamDataCascade() {
        repository.deleteAll();
    }

    @Override
    public void addExam(ExamDTO examDTO) {
        repository.save(examDTOMapper.toDomain(examDTO));
    }

    private Exam findById(UUID examId) {
        return repository.findById(examId)
                .orElseThrow(NotFoundException::new);
    }

    private List<Exam> findAll() {
        return repository.findAll()
                .stream().toList();
    }
}
