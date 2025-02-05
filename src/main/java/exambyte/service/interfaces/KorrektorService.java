package exambyte.service.interfaces;

import exambyte.domain.aggregate.user.Korrektor;

import java.util.UUID;

public interface KorrektorService {

    Korrektor getKorrektor(UUID fachId);

    void saveKorrektor(Korrektor korrektor);

    Korrektor getKorrektorByName(String name);
}
