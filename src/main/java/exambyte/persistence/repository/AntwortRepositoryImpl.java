package exambyte.persistence.repository;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.persistence.entities.AntwortEntity;
import exambyte.persistence.mapper.AntwortMapper;
import exambyte.domain.repository.AntwortRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AntwortRepositoryImpl implements AntwortRepository {

    private final AntwortMapper antwortMapper = new AntwortMapper();
    private final SpringDataAntwortRepository springDataAntwortRepository;

    public AntwortRepositoryImpl(SpringDataAntwortRepository springDataAntwortRepository) {
        this.springDataAntwortRepository = springDataAntwortRepository;
    }

    @Override
    public Optional<Antwort> findByFachId(UUID id) {
        Optional<AntwortEntity> entity = springDataAntwortRepository.findByFachId(id);
        return entity.map(AntwortMapper::toDomain);
    }

    @Override
    public void save(Antwort antwort) {
        AntwortEntity antwortEntity = antwortMapper.toEntity(antwort);
        springDataAntwortRepository.save(antwortEntity);
    }
}
