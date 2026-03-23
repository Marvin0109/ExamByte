package exambyte.infrastructure.repository;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.mapper.StudentMapper;
import exambyte.infrastructure.entity.StudentEntity;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentMapper mapper;
    private final StudentDAO dao;

    public StudentRepositoryImpl(StudentDAO dao, StudentMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public Optional<Student> findById(UUID id) {
        Optional<StudentEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Student student) {
        StudentEntity entity = mapper.toEntity(student);
        dao.save(entity);
    }

    @Override
    public Optional<Student> findByName(String name) {
        Optional<StudentEntity> entity = dao.findByName(name);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<UUID> findIdByName(String name) {
        StudentEntity loaded = dao.findIdByName(name).orElse(null);
        if (loaded != null) return Optional.of(loaded.getId());
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
