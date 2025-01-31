package exambyte.domain.aggregate.user;

import java.util.UUID;

public class Professor implements Person {

    private final Long id;
    private final UUID fachId;
    private final String name;

    private Professor(Long id, UUID fachId, String name) {
        this.fachId = fachId;
        this.id = id;
        this.name = name;
    }

    // Factory Methode
    public static Professor of(Long id, UUID fachId, String name) {
        return new Professor(id, fachId != null ? fachId : UUID.randomUUID(), name);
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