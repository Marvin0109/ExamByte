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

    private final StudentMapper mapper;
    private final StudentDAO dao;

    public StudentRepositoryImpl(StudentDAO studentDAO, StudentMapper studentMapper) {
        this.dao = studentDAO;
        this.mapper = studentMapper;
    }

    @Override
    public Optional<Student> findByFachId(UUID fachId) {
        Optional<StudentEntity> entity = dao.findByFachId(fachId);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Student student) {
        StudentEntity studentEntity = mapper.toEntity(student);
        dao.save(studentEntity);
    }

    @Override
    public Optional<Student> findByName(String name) {
        Optional<StudentEntity> entity = dao.findByName(name);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<UUID> findFachIdByName(String name) {
        StudentEntity loaded =  dao.findFachIdByName(name).orElse(null);
        if (loaded != null) return Optional.of(loaded.getFachId());
        return Optional.empty();
    }
}
