package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentMapper studentMapper;
    private final StudentDAO studentDAO;

    public StudentRepositoryImpl(StudentDAO studentDAO, StudentMapper studentMapper) {
        this.studentDAO = studentDAO;
        this.studentMapper = studentMapper;
    }

    @Override
    public Optional<Student> findByFachId(UUID fachId) {
        Optional<StudentEntity> entity = studentDAO.findByFachId(fachId);
        return entity.map(studentMapper::toDomain);
    }

    @Override
    public void save(Student student) {
        StudentEntity studentEntity = studentMapper.toEntity(student);
        studentDAO.save(studentEntity);
    }

    @Override
    public Optional<Student> findByName(String name) {
        Optional<StudentEntity> entity = studentDAO.findByName(name);
        return entity.map(studentMapper::toDomain);
    }

    @Override
    public Optional<UUID> findFachIdByName(String name) {
        StudentEntity loaded =  studentDAO.findFachIdByName(name).orElse(null);
        if (loaded != null) return Optional.of(loaded.getFachId());
        return Optional.empty();
    }
}
