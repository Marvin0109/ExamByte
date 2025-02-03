package exambyte.service;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KorrektorService {

    private final KorrektorRepository korrektorRepository;

    public KorrektorService(KorrektorRepository korrektorRepository) {
        this.korrektorRepository = korrektorRepository;
    }

    public Korrektor getKorrektor(UUID fachId) {
        return korrektorRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    public UUID saveKorrektor(Korrektor korrektor) {
        korrektorRepository.save(korrektor);
        return korrektor.uuid();
    }

    public Korrektor getKorrektorByName(String name) {
        return korrektorRepository.findByName(name)
                .orElseThrow(NichtVorhandenException::new);
    }
}
