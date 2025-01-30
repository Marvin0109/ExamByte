package exambyte.domain.aggregate.user;

import java.util.UUID;

public class Student implements Person {

    private final Long id;
    private final UUID uuid;
    private final String name;

    private Student(Long id, UUID uuid, String name) {
        this.uuid = uuid;
        this.id = id;
        this.name = name;
    }

    //Factory Methode
    public static Student of(Long id, UUID uuid, String name) {
        return new Student(id, UUID.randomUUID(), name);
    }

    @Override
    public UUID uuid() { return uuid; }
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}