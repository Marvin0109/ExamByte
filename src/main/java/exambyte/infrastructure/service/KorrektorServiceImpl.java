package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.domain.service.KorrektorService;
import exambyte.infrastructure.exceptions.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class KorrektorServiceImpl  implements KorrektorService {

    private final KorrektorRepository repository;

    public KorrektorServiceImpl(KorrektorRepository korrektorRepository) {
        this.repository = korrektorRepository;
    }

    @Override
    public Korrektor getKorrektor(UUID id) {
        return repository.findById(id)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveKorrektor(String name) {
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
            .name(name)
            .build();

        repository.save(korrektor);
    }

    @Override
    public Optional<Korrektor> getKorrektorByName(String name) {
        return repository.findByName(name);
    }
}
