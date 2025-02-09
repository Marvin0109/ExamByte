package exambyte.domain.service;

import exambyte.domain.aggregate.user.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentService {

    Student getStudent(UUID fachId);

    void saveStudent(String name);

    Optional<Student> getStudentByName(String name);

    UUID getStudentFachId(String name);
}
