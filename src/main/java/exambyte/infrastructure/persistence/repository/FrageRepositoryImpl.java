package exambyte.infrastructure.persistence.repository;

import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.entitymapper.FrageMapper;
import exambyte.infrastructure.persistence.entities.FrageEntity;
import exambyte.infrastructure.persistence.entities.ProfessorEntity;
import exambyte.domain.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final FrageMapper frageMapper;
    private final SpringDataFrageRepository springDataFrageRepository;
    private final SpringDataProfessorRepository springDataProfessorRepository;

    @Autowired
    public FrageRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository,
                               SpringDataFrageRepository springDataFrageRepository, FrageMapper frageMapper) {
        this.springDataProfessorRepository = springDataProfessorRepository;
        this.springDataFrageRepository = springDataFrageRepository;
        this.frageMapper = frageMapper;
    }

    @Override
    public Collection<Frage> findAll() {
        return springDataFrageRepository.findAll()
                .stream()
                .map(frageMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Frage> findByFachId(UUID fachId) {
        Optional<FrageEntity> entity = springDataFrageRepository.findByFachId(fachId);
        return entity.map(frageMapper::toDomain);
    }

    @Override
    public void save(Frage frage) {
        FrageEntity entity = frageMapper.toEntity(frage);
        springDataFrageRepository.save(entity);
    }

    @Override
    public List<Frage> findByExamFachId(UUID examId) {
        return springDataFrageRepository.findByExamFachId(examId).stream()
                .map(frageMapper::toDomain)
                .toList();
    }

    public ProfessorEntity findByProfFachId(UUID profFachId) {
        Optional<ProfessorEntity> existingProfessor = springDataProfessorRepository.findByFachId(profFachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntity newProfessor = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(profFachId)
                .name("")
                .build();
        springDataProfessorRepository.save(newProfessor);
        return newProfessor;
    }
}
