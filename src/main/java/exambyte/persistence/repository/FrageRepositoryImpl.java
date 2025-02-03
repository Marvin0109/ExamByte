package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Frage;
import exambyte.persistence.entities.FrageEntity;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.FrageMapper;
import exambyte.domain.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final FrageMapper frageMapper = new FrageMapper();
    private final SpringDataFrageRepository springDataFrageRepository;
    private final SpringDataProfessorRepository springDataProfessorRepository;

    @Autowired
    public FrageRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository,
                               SpringDataFrageRepository springDataFrageRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
        this.springDataFrageRepository = springDataFrageRepository;
    }

    @Override
    public Collection<Frage> findAll() {
        return springDataFrageRepository.findAll()
                .stream()
                .map(FrageMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Frage> findByFachId(UUID fachId) {
        Optional<FrageEntity> entity = springDataFrageRepository.findByFachId(fachId);
        return entity.map(FrageMapper::toDomain);
    }

    @Override
    public void save(Frage frage) {
        FrageEntity entity = frageMapper.toEntity(frage);
        springDataFrageRepository.save(entity);
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
