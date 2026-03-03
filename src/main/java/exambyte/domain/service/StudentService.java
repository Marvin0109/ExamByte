package exambyte.domain.service;

import exambyte.domain.model.aggregate.user.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentService {

    Student getStudent(UUID id);

    void saveStudent(String name);

    Optional<Student> getStudentByName(String name);

    UUID getStudentId(String name);
}
