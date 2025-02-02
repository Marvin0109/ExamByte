package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;
import exambyte.persistence.mapper.StudentMapper;
import exambyte.domain.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentMapper studentMapper = new StudentMapper();
    private final SpringDataStudentRepository springDataStudentRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public Optional<Student> findByFachId(UUID fachId) {
        Optional<StudentEntity> entity = springDataStudentRepository.findByFachId(fachId);
        return entity.map(StudentMapper::toDomain);
    }

    @Override
    public void save(Student student) {
        StudentEntity studentEntity = studentMapper.toEntity(student);
        springDataStudentRepository.save(studentEntity);
    }


}
