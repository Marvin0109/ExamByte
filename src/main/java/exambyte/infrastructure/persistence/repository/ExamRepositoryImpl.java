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

    private final ExamMapper mapper;
    private final ExamDAO dao;

    public ExamRepositoryImpl(ExamDAO testRepository, ExamMapper examMapper) {
        this.dao = testRepository;
        this.mapper = examMapper;
    }

    @Override
    public Collection<Exam> findAll() {
        return dao.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Exam> findById(UUID id) {
        Optional<ExamEntity> entity = dao.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public void save(Exam examEntity) {
        ExamEntity entity = mapper.toEntity(examEntity);
        dao.save(entity);
    }

    @Override
    public Optional<UUID> findByStartTime(LocalDateTime start) {
        ExamEntity loaded =  dao.findByStart(start).orElse(null);
        if (loaded != null) return Optional.of(loaded.getId());
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID id) {
        dao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
