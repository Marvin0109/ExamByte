package exambyte.persistence.entities;

import jakarta.persistence.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class StudentEntity {
    @Id
    private final Integer id;
    private final String name;

    @PersistenceCreator
    public StudentEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
