package exambyte.service.impl;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.service.interfaces.KorrektorService;
import exambyte.service.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KorrektorServiceImpl  implements KorrektorService {

    private final KorrektorRepository korrektorRepository;

    public KorrektorServiceImpl(KorrektorRepository korrektorRepository) {
        this.korrektorRepository = korrektorRepository;
    }

    public Korrektor getKorrektor(UUID fachId) {
        return korrektorRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    public void saveKorrektor(Korrektor korrektor) {
        korrektorRepository.save(korrektor);
    }

    public Korrektor getKorrektorByName(String name) {
        return korrektorRepository.findByName(name)
                .orElseThrow(NichtVorhandenException::new);
    }
}
