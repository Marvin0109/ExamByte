package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import exambyte.service.AntwortRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AntwortRepositoryImpl implements AntwortRepository {

    private final SpringDataAntwortRepository springDataAntwortRepository;

    public AntwortRepositoryImpl(@Lazy SpringDataAntwortRepository springDataAntwortRepository) {
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
