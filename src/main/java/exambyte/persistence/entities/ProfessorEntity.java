package exambyte.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.PersistenceCreator;
@Entity
@Table(name ="professor")
public class ProfessorEntity {

    @Id
    private final Integer id;
    private final String name;

    protected ProfessorEntity() {
        this.id = 0;
        this.name = "";
    }
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
