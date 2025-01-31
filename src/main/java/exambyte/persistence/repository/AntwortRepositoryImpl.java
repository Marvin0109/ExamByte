package exambyte.persistence.repository;

import exambyte.persistence.entities.JDBC.AntwortEntityJDBC;
import exambyte.persistence.entities.JDBC.FrageEntityJDBC;
import exambyte.service.AntwortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AntwortRepositoryImpl implements AntwortRepository {

    private final SpringDataAntwortRepository springDataAntwortRepository;

    public AntwortRepositoryImpl(SpringDataAntwortRepository springDataAntwortRepository) {
        this.springDataAntwortRepository = springDataAntwortRepository;
    }

    @Override
    public Optional<AntwortEntityJDBC> findByFachId(UUID id) {
        return springDataAntwortRepository.findByFachId(id);
    }

    @Override
    public void save(AntwortEntityJDBC entity) {
        springDataAntwortRepository.save(entity);
    }
}
