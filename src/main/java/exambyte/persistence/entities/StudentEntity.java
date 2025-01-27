package exambyte.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.security.core.parameters.P;

@Entity
@Table(name = "student")
public class StudentEntity {

    @Id
    private final Long id;
    private final String name;

    public StudentEntity() {
        this(null, "");
    }

    @PersistenceCreator
    public StudentEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
