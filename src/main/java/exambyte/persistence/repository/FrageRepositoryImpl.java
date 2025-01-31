package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.persistence.entities.JDBC.ProfessorEntityJDBC;
import exambyte.service.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final SpringDataFrageRepository springDataFrageRepository;
    private final SpringDataProfessorRepository springDataProfessorRepository;

    @Autowired
    public FrageRepositoryImpl(SpringDataProfessorRepository springDataProfessorRepository,
                               SpringDataFrageRepository springDataFrageRepository) {
        this.springDataProfessorRepository = springDataProfessorRepository;
        this.springDataFrageRepository = springDataFrageRepository;
    }

    @Override
    public Optional<FrageEntityJDBC> findByFachId(UUID fachId) {
        return springDataFrageRepository.findByFachId(fachId);
    }

    @Override
    public void save(FrageEntityJDBC frageEntityJDBC) {
        springDataFrageRepository.save(frageEntityJDBC);
    }

    public ProfessorEntityJDBC findByProfFachId(UUID profFachId) {
        Optional<ProfessorEntityJDBC> existingProfessor = springDataProfessorRepository.findByFachId(profFachId);

        if (existingProfessor.isPresent()) {
            return existingProfessor.get();
        }

        ProfessorEntityJDBC newProfessor = new ProfessorEntityJDBC(null, profFachId, "N/A");
        springDataProfessorRepository.save(newProfessor);
        return newProfessor;
    }
}
