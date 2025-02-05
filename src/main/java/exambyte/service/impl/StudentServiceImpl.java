package exambyte.service;

import exambyte.domain.aggregate.user.Student;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudent(UUID fachId) {
        return studentRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Student getStudentByName(String name) {
        return studentRepository.findByName(name)
                .orElseThrow(NichtVorhandenException::new);
    }

    public UUID getStudentFachId(String name) {
        return studentRepository.findFachIdByName(name);
    }
}
