package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Professor;
import exambyte.domain.entitymapper.ProfessorMapper;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.ProfessorMapperImpl;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final ProfessorMapper professorMapper;
    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository,
                                   ProfessorMapper professorMapper) {
        this.springDataProfessorRepository = springDataProfessorRepository;
        this.professorMapper = professorMapper;
    }

    @Override
    public Optional<Professor> findByFachId(UUID fachId) {
        Optional<ProfessorEntity> entity = springDataProfessorRepository.findByFachId(fachId);
        return entity.map(professorMapper::toDomain);
    }

    @Override
    public void save(Professor professor) {
        ProfessorEntity entity = professorMapper.toEntity(professor);
        springDataProfessorRepository.save(entity);
    }

    @Override
    public Optional<Professor> findByName(String name) {
        Optional<ProfessorEntity> entity = springDataProfessorRepository.findByName(name);
        return entity.map(professorMapper::toDomain);
    }

    @Override
    public UUID findFachIdByName(String name) {
        return springDataProfessorRepository.findFachIdByName(name);
    }

    public ProfessorEntity findByProfFachId(UUID fachId) {
        Optional<Professor> professor = findByFachId(fachId);

        if (professor.isPresent()) {
            return new ProfessorEntity.ProfessorEntityBuilder()
                    .id(null)
                    .fachId(fachId)
                    .name(professor.get().getName())
                    .build();
        }

        return new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(fachId)
                .name("")
                .build();
    }
}
