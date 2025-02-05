package exambyte.service.impl;

import exambyte.domain.aggregate.user.Korrektor;
import exambyte.domain.repository.KorrektorRepository;
import exambyte.application.interfaces.KorrektorService;
import exambyte.service.NichtVorhandenException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KorrektorServiceImpl  implements KorrektorService {

    private final KorrektorRepository korrektorRepository;

    public KorrektorServiceImpl(KorrektorRepository korrektorRepository) {
        this.korrektorRepository = korrektorRepository;
    }

    @Override
    public Korrektor getKorrektor(UUID fachId) {
        return korrektorRepository.findByFachId(fachId)
                .orElseThrow(NichtVorhandenException::new);
    }

    @Override
    public void saveKorrektor(String name) {
        Korrektor korrektor = new Korrektor.KorrektorBuilder()
                .id(null)
                .fachId(null)
                .name(name)
                .build();

        korrektorRepository.save(korrektor);
    }

    @Override
    public Korrektor getKorrektorByName(String name) {
        return korrektorRepository.findByName(name)
                .orElseThrow(NichtVorhandenException::new);
    }
}
