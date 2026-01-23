package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import exambyte.domain.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final FrageMapper frageMapper;
    private final FrageDAO frageDAO;

    @Autowired
    public FrageRepositoryImpl(FrageDAO frageDAO, FrageMapper frageMapper) {
        this.frageDAO = frageDAO;
        this.frageMapper = frageMapper;
    }

    @Override
    public Collection<Frage> findAll() {
        return frageDAO.findAll()
                .stream()
                .map(frageMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Frage> findByFachId(UUID fachId) {
        Optional<FrageEntity> entity = frageDAO.findByFachId(fachId);
        return entity.map(frageMapper::toDomain);
    }

    @Override
    public UUID save(Frage frage) {
        FrageEntity entity = frageMapper.toEntity(frage);
        frageDAO.save(entity);
        return entity.getFachId();
    }

    @Override
    public List<Frage> findByExamFachId(UUID examId) {
        return frageDAO.findByExamFachId(examId).stream()
                .map(frageMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        frageDAO.deleteAll();
    }
}
