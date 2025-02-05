package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Professor;
import exambyte.persistence.entities.ProfessorEntity;
import exambyte.persistence.mapper.ProfessorMapper;
import exambyte.domain.repository.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final ProfessorMapper professorMapper = new ProfessorMapper();
    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
    }

    @Override
    public Optional<Professor> findByFachId(UUID fachId) {
        Optional<ProfessorEntity> entity = springDataProfessorRepository.findByFachId(fachId);
        return entity.map(ProfessorMapper::toDomain);
    }

    @Override
    public void save(Professor professor) {
        ProfessorEntity entity = professorMapper.toEntity(professor);
        springDataProfessorRepository.save(entity);
    }

    @Override
    public Optional<Professor> findByName(String name) {
        Optional<ProfessorEntity> entity = springDataProfessorRepository.findByName(name);
        return entity.map(ProfessorMapper::toDomain);
    }

    @Override
    public UUID getFachId(String name) {
        return springDataProfessorRepository.getFachId(name);
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
