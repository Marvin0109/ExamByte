package exambyte.persistence.repository;

import exambyte.persistence.entities.KorrektorEntity;
import exambyte.service.KorrektorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class KorrektorRepositoryImpl implements KorrektorRepository {

    private final SpringDataKorrektorRepository springDataKorrektorRepository;

    public KorrektorRepositoryImpl(SpringDataKorrektorRepository springDataKorrektorRepository) {
        this.springDataKorrektorRepository = springDataKorrektorRepository;
    }

    @Override
    public Optional<KorrektorEntity> findByFachId(UUID fachId) {
        return springDataKorrektorRepository.findByFachId(fachId);
    }

    @Override
    public void save(KorrektorEntity korrektorEntity) {
        springDataKorrektorRepository.save(korrektorEntity);
    }

    public KorrektorEntity findByKorrFachId(UUID fachId) {
        Optional<KorrektorEntity> existingKorrektor = findByFachId(fachId);

        if (existingKorrektor.isPresent()) {
            return existingKorrektor.get();
        }

        KorrektorEntity newKorrektor = new KorrektorEntity.KorrektorEntityBuilder()
                .id(null)
                .fachId(fachId)
                .name("")
                .build();
        save(newKorrektor);
        return newKorrektor;
    }
}
