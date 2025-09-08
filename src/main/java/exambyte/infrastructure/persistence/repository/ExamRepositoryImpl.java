package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.entitymapper.ExamMapper;
import exambyte.infrastructure.persistence.entities.ExamEntity;
import exambyte.domain.repository.ExamRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    private final ExamMapper examMapper;
    private final ExamDAO repository;

    public ExamRepositoryImpl(ExamDAO testRepository, ExamMapper examMapper) {
        this.repository = testRepository;
        this.examMapper = examMapper;
    }

    @Override
    public Collection<Exam> findAll() {
        return repository.findAll()
                .stream()
                .map(examMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Exam> findByFachId(UUID fachId) {
        Optional<ExamEntity> entity = repository.findByFachId(fachId);
        return entity.map(examMapper::toDomain);
    }

    @Override
    public void save(Exam examEntity) {
        ExamEntity entity = examMapper.toEntity(examEntity);
        repository.save(entity);
    }

    @Override
    public Optional<UUID> findByStartTime(LocalDateTime startTime) {
        ExamEntity loaded =  repository.findByStartZeitpunkt(startTime).orElse(null);
        if (loaded != null) return Optional.of(loaded.getFachId());
        return Optional.empty();
    }

    @Override
    public void deleteByFachId(UUID id) {
        repository.deleteByFachId(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
