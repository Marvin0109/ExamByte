package exambyte.application.service.query;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.mapper.ExamDTOMapper;
import exambyte.application.mapper.QuestionDTOMapper;
import exambyte.domain.service.ExamService;
import exambyte.domain.service.QuestionService;
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
    private final QuestionService questionService;
    private final AnswerService answerService;

    private final ExamDTOMapper examDTOMapper;
    private final QuestionDTOMapper questionDTOMapper;

    public ExamQueryServiceImpl(ExamService examService,
                                StudentService studentService,
                                QuestionService questionService,
                                AnswerService answerService,
                                ExamDTOMapper examDTOMapper,
                                QuestionDTOMapper questionDTOMapper) {
        this.examService = examService;
        this.studentService = studentService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.examDTOMapper = examDTOMapper;
        this.questionDTOMapper = questionDTOMapper;
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
                    .equals(examDTO.start().truncatedTo(ChronoUnit.MINUTES))) {
                return examDTO.id();
            }
        }

        return null;
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return examService.allExams().stream()
                .map(examDTOMapper::toDTO)
                .sorted(Comparator.comparing(ExamDTO::start))
                .toList();
    }

    @Override
    public boolean hasStudentSubmittedExam(UUID examId, String studentName) {
        UUID studentId = studentService.getStudentIdByName(studentName);
        List<QuestionDTO> questions = questionDTOMapper.toQuestionDTOList(questionService.getQuestionsForExam(examId));

        return questions.stream()
                .anyMatch(question ->
                        answerService.findByStudentAndQuestion(studentId, question.id()) != null);
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
