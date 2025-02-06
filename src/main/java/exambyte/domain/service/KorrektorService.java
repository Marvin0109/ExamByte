package exambyte.domain.service;

import exambyte.domain.aggregate.user.Korrektor;

import java.util.UUID;

public interface KorrektorService {

    Korrektor getKorrektor(UUID fachId);

    void saveKorrektor(String name);

    Korrektor getKorrektorByName(String name);
}
