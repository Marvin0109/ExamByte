package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.persistence.entities.StudentEntity;
import exambyte.persistence.mapper.StudentMapperImpl;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentMapper studentMapper;
    private final SpringDataStudentRepository springDataStudentRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository, StudentMapper studentMapper) {
        this.springDataStudentRepository = springDataStudentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public Optional<Student> findByFachId(UUID fachId) {
        Optional<StudentEntity> entity = springDataStudentRepository.findByFachId(fachId);
        return entity.map(studentMapper::toDomain);
    }

    @Override
    public void save(Student student) {
        StudentEntity studentEntity = studentMapper.toEntity(student);
        springDataStudentRepository.save(studentEntity);
    }

    @Override
    public Optional<Student> findByName(String name) {
        Optional<StudentEntity> entity = springDataStudentRepository.findByName(name);
        return entity.map(studentMapper::toDomain);
    }

    @Override
    public UUID findFachIdByName(String name) {
        return springDataStudentRepository.findFachIdByName(name);
    }
}
