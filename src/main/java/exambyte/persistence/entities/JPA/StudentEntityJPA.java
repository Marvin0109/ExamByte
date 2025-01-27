package exambyte.persistence.entities.JPA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "student")
public class StudentEntityJPA {

    @Id
    private final Long id;
    private final String name;

    public StudentEntityJPA() {
        this(null, "");
    }

    @PersistenceCreator
    public StudentEntityJPA(Long id, String name) {
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