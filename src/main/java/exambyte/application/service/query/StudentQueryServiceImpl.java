package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.domain.mapper.StudentDTOMapper;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentQueryServiceImpl implements StudentQueryService {

    private final AnswerQueryService answerQueryService;
    private final StudentService studentService;
    private final StudentDTOMapper studentDTOMapper;

    public StudentQueryServiceImpl(AnswerQueryService answerQueryService,
                                   StudentService studentService,
                                   StudentDTOMapper studentDTOMapper) {
        this.answerQueryService = answerQueryService;
        this.studentService = studentService;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        List<AnswerDTO> answers = answerQueryService.getFreeResponseAnswersForExam(examId);

        return answers.stream()
                .collect(Collectors.toMap(
                        AnswerDTO::studentId,
                        a -> studentService.getStudent(a.studentId()),
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .map(studentDTOMapper::toDTO)
                .toList();
    }

    @Override
    public UUID getStudentIdByName(String studentName) {
        return studentService.getStudentId(studentName);
    }
}
