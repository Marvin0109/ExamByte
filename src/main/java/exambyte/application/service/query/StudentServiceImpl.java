package exambyte.application.service.query;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.StudentDTO;
import exambyte.application.exception.NotFoundException;
import exambyte.application.mapper.StudentDTOMapper;
import exambyte.domain.model.user.Student;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final AnswerService answerService;
    private final StudentRepository repository;
    private final StudentDTOMapper mapper;

    public StudentServiceImpl(AnswerService answerService,
                              StudentRepository repository,
                              StudentDTOMapper mapper) {
        this.answerService = answerService;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<StudentDTO> getStudentSubmittedExam(UUID examId) {
        List<AnswerDTO> answers = answerService.getFreeResponseAnswersForExam(examId);

        return answers.stream()
                .collect(Collectors.toMap(
                        AnswerDTO::studentId,
                        a -> getStudent(a.studentId()),
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public UUID getStudentIdByName(String studentName) {
        return getStudentId(studentName);
    }

    @Override
    public void saveStudent(String name) {
        Student student = new Student.StudentBuilder()
                .name(name)
                .build();
        repository.save(student);
    }

    @Override
    public Optional<StudentDTO> getStudentByName(String name) {
        Optional<Student> student = getByName(name);
        return student.map(mapper::toDTO);
    }

    private Student getStudent(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    private UUID getStudentId(String name) {
        Optional<UUID> loadedId = repository.findIdByName(name);
        return loadedId.orElse(null);
    }

    private Optional<Student> getByName(String name) {
        return repository.findByName(name);
    }
}
