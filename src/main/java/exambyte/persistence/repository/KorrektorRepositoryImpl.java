package exambyte.persistence.repository;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.entitymapper.KorrektorMapper;
import exambyte.persistence.entities.KorrektorEntity;
import exambyte.domain.repository.KorrektorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class KorrektorRepositoryImpl implements KorrektorRepository {

    private final KorrektorMapper korrektorMapper;
    private final SpringDataKorrektorRepository springDataKorrektorRepository;

    public KorrektorRepositoryImpl(SpringDataKorrektorRepository springDataKorrektorRepository,
                                   KorrektorMapper korrektorMapper) {
        this.springDataKorrektorRepository = springDataKorrektorRepository;
        this.korrektorMapper = korrektorMapper;
    }

    @Override
    public Optional<Korrektor> findByFachId(UUID fachId) {
        Optional<KorrektorEntity> entity = springDataKorrektorRepository.findByFachId(fachId);
        return entity.map(korrektorMapper::toDomain);
    }

    @Override
    public void save(Korrektor korrektor) {
        KorrektorEntity entity = korrektorMapper.toEntity(korrektor);
        springDataKorrektorRepository.save(entity);
    }

    @Override
    public Optional<Korrektor> findByName(String name) {
        Optional<KorrektorEntity> entity = springDataKorrektorRepository.findByName(name);
        return entity.map(korrektorMapper::toDomain);
    }

    public KorrektorEntity findByKorFachId(UUID fachId) {
        Optional<Korrektor> korrektor = findByFachId(fachId);

        if (korrektor.isPresent()) {
            return new KorrektorEntity.KorrektorEntityBuilder()
                    .id(null)
                    .fachId(fachId)
                    .name(korrektor.get().getName())
                    .build();
        }

        return new KorrektorEntity.KorrektorEntityBuilder()
                .id(null)
                .fachId(fachId)
                .name("")
                .build();
    }
}
