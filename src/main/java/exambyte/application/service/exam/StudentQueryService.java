package exambyte.application.service.exam;

import exambyte.application.dto.StudentDTO;

import java.util.List;
import java.util.UUID;

public interface StudentQueryService {

    List<StudentDTO> getStudentSubmittedExam(UUID examId);

    UUID getStudentIdByName(String studentName);
}
