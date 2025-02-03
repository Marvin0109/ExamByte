package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.persistence.entities.ExamEntity;
import exambyte.persistence.mapper.ExamMapper;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    private final ExamMapper examMapper = new ExamMapper();
    private final SpringDataExamRepository testRepository;

    public ExamRepositoryImpl(SpringDataExamRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Collection<Exam> findAll() {
        return testRepository.findAll()
                .stream()
                .map(ExamMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Exam> findByFachId(UUID fachId) {
        Optional<ExamEntity> entity = testRepository.findByFachId(fachId);
        return entity.map(ExamMapper::toDomain);
    }

    @Override
    public void save(Exam examEntity) {
        ExamEntity entity = examMapper.toEntity(examEntity);
        testRepository.save(entity);
    }
}
