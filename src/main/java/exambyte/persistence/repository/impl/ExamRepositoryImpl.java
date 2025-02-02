package exambyte.persistence.repository.impl;

import exambyte.persistence.entities.ExamEntity;
import exambyte.persistence.repository.SpringDataExamRepository;
import exambyte.service.repository.api.ExamRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    private final SpringDataExamRepository testRepository;

    public ExamRepositoryImpl(SpringDataExamRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Optional<ExamEntity> findByFachId(UUID fachId) {
        return testRepository.findByFachId(fachId);
    }

    @Override
    public void save(ExamEntity examEntity) {
        testRepository.save(examEntity);
    }
}
