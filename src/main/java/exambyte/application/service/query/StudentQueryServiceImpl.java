package exambyte.application.service.query;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.domain.mapper.StudentDTOMapper;
import exambyte.domain.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentQueryServiceImpl implements StudentQueryService {

    private final AntwortQueryService antwortQueryService;
    private final StudentService studentService;
    private final StudentDTOMapper studentDTOMapper;

    public StudentQueryServiceImpl(AntwortQueryService antwortQueryService,
                                   StudentService studentService,
                                   StudentDTOMapper studentDTOMapper) {
        this.antwortQueryService = antwortQueryService;
        this.studentService = studentService;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        List<AntwortDTO> antworten = antwortQueryService.getFreeResponseAntwortenForExam(examId);

        return antworten.stream()
                .collect(Collectors.toMap(
                        AntwortDTO::studentId,
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
