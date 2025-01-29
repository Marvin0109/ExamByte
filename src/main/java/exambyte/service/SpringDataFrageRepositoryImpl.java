package exambyte.service;

import exambyte.persistence.repository.SpringDataFrageRepository;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataFrageRepositoryImpl implements FrageRepository {

    private final SpringDataFrageRepository springDataFrageRepository;

    public SpringDataFrageRepositoryImpl(@Lazy SpringDataFrageRepository springDataFrageRepository) {
        this.springDataFrageRepository = springDataFrageRepository;
    }

    @Override
    public Optional<FrageEntityJDBC> findById(Long id) {
        return springDataFrageRepository.findById(id);
    }

    @Override
    public void save(FrageEntityJDBC frageEntityJDBC) {
        springDataFrageRepository.save(frageEntityJDBC);
    }
}
