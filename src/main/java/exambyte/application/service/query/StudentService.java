package exambyte.application.service.query;

import exambyte.application.dto.StudentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentService {

    List<StudentDTO> getStudentSubmittedExam(UUID examId);

    UUID getStudentIdByName(String studentName);

    Optional<StudentDTO> getStudentByName(String name);

    void saveStudent(String name);
}
