package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.service.FrageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FrageRepositoryImpl implements FrageRepository {

    private final SpringDataFrageRepository springDataFrageRepository;

    public FrageRepositoryImpl(@Lazy SpringDataFrageRepository springDataFrageRepository) {
        this.springDataFrageRepository = springDataFrageRepository;
    }

    @Override
    public Optional<FrageEntityJDBC> findByFachId(UUID id) {
        return springDataFrageRepository.findByFachId(id);
    }

    @Override
    public void save(FrageEntityJDBC frageEntityJDBC) {
        springDataFrageRepository.save(frageEntityJDBC);
    }
}
