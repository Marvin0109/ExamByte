package exambyte.persistence.repository.impl;

import exambyte.persistence.entities.StudentEntity;
import exambyte.persistence.repository.SpringDataStudentRepository;
import exambyte.service.repository.api.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final SpringDataStudentRepository springDataStudentRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository springDataStudentRepository) {
        this.springDataStudentRepository = springDataStudentRepository;
    }

    @Override
    public Optional<StudentEntity> findByFachId(UUID fachId) {
        return springDataStudentRepository.findByFachId(fachId);
    }

    @Override
    public void save(StudentEntity studentEntity) {
        springDataStudentRepository.save(studentEntity);
    }


}
