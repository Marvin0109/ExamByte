package exambyte.domain.service;

import exambyte.domain.aggregate.user.Student;

import java.util.UUID;

public interface StudentService {

    Student getStudent(UUID fachId);

    void saveStudent(String name);

    Student getStudentByName(String name);

    UUID getStudentFachId(String name);
}
