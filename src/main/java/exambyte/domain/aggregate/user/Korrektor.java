package exambyte.domain.aggregate.user;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Korrektor implements Person {

    private final Long id;
    private final UUID fachId;
    private final String name;

    private Korrektor(Long id, UUID fachId, String name) {
        this.id = id;
        this.fachId = fachId;
        this.name = name;
    }

    // Factory Methode
    public static Korrektor of(Long id, UUID fachId, String name) {
        return new Korrektor(id, fachId != null ? fachId : UUID.randomUUID(), name);
    }

    @Override
    public UUID uuid() {
        return fachId;
    }
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}
