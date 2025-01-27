package exambyte.persistence.entities.JPA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name ="professor")
public class ProfessorEntityJPA {

    @Id
    private final Long id;
    private final String name;

    public ProfessorEntityJPA() {
        this(null, "");
    }

    @PersistenceCreator
    public ProfessorEntityJPA(Long id, String name) {
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
