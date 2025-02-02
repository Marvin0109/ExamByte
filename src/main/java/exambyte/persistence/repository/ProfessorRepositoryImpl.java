package exambyte.persistence.repository;

import exambyte.persistence.entities.ProfessorEntity;
import exambyte.service.ProfessorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final SpringDataProfessorRepository springDataProfessorRepository;

    public ProfessorRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
    }

    @Override
    public Optional<ProfessorEntity> findByFachId(UUID fachId) {
        return springDataProfessorRepository.findByFachId(fachId);
    }

    @Override
    public void save(ProfessorEntity professorEntity) {
        springDataProfessorRepository.save(professorEntity);
    }

    public ProfessorEntity findByProfFachId(UUID fachId) {
        Optional<ProfessorEntity> existingProfessor = findByFachId(fachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntity newProfessor = new ProfessorEntity.ProfessorEntityBuilder()
                .id(null)
                .fachId(fachId)
                .name("")
                .build();
        save(newProfessor);
        return newProfessor;
    }
}
