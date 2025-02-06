package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.entitymapper.ExamMapper;
import exambyte.persistence.entities.ExamEntity;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    private final ExamMapper examMapper;
    private final SpringDataExamRepository testRepository;

    public ExamRepositoryImpl(SpringDataExamRepository testRepository, ExamMapper examMapper) {
        this.testRepository = testRepository;
        this.examMapper = examMapper;
    }

    @Override
    public Collection<Exam> findAll() {
        return testRepository.findAll()
                .stream()
                .map(examMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Exam> findByFachId(UUID fachId) {
        Optional<ExamEntity> entity = testRepository.findByFachId(fachId);
        return entity.map(examMapper::toDomain);
    }

    @Override
    public void save(Exam examEntity) {
        ExamEntity entity = examMapper.toEntity(examEntity);
        testRepository.save(entity);
    }
}
