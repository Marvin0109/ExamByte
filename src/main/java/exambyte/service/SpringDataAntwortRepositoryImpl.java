package exambyte.service;

import exambyte.persistence.repository.SpringDataAntwortRepository;
import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataAntwortRepositoryImpl implements AntwortRepository {

    private final SpringDataAntwortRepository springDataAntwortRepository;

    public SpringDataAntwortRepositoryImpl(@Lazy SpringDataAntwortRepository springDataAntwortRepository) {
        this.springDataAntwortRepository = springDataAntwortRepository;
    }

    @Override
    public Optional<AntwortEntityJDBC> findById(Long id) {
        return springDataAntwortRepository.findById(id);
    }

    @Override
    public void save(AntwortEntityJDBC entity) {
        springDataAntwortRepository.save(entity);
    }
}
