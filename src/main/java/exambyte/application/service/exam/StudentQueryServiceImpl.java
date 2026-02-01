package exambyte.application.service.exam;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.application.service.submission.AnswerSubmissionService;
import exambyte.domain.mapper.StudentDTOMapper;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentQueryServiceImpl implements StudentQueryService {

    private final AnswerSubmissionService answerSubmissionService;
    private final StudentService studentService;
    private final StudentDTOMapper studentDTOMapper;

    public StudentQueryServiceImpl(AnswerSubmissionService answerSubmissionService,
                                   StudentService studentService,
                                   StudentDTOMapper studentDTOMapper) {
        this.answerSubmissionService = answerSubmissionService;
        this.studentService = studentService;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        List<AntwortDTO> antworten = answerSubmissionService.getFreitextAntwortenForExam(examId);

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
}
