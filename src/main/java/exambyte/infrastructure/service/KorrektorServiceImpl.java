package exambyte.infrastructure.service;

import exambyte.domain.model.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.domain.service.KorrektorService;
import exambyte.infrastructure.NichtVorhandenException;
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
    public Korrektor getKorrektor(UUID fachId) {
        return repository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveKorrektor(String name) {
        if (name.equals("Automatischer Korrektor")) {
            UUID automatic = UUID.fromString("11111111-1111-1111-1111-111111111111");
            Korrektor korrektor = new Korrektor.KorrektorBuilder()
                    .id(null)
                    .fachId(automatic)
                    .name(name)
                    .build();

            repository.save(korrektor);
        } else {
            Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();

            repository.save(korrektor);
        }
    }

    @Override
    public Optional<Korrektor> getKorrektorByName(String name) {
        return repository.findByName(name);
    }
}
