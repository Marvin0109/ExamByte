package exambyte.persistence.entities;

import exambyte.domain.aggregate.user.Professor;
import jakarta.persistence.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class ProfessorEntity {

    @Id
    private final Integer id;
    private final String name;

    @PersistenceCreator
    public ProfessorEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
