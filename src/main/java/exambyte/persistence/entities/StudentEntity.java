package exambyte.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.PersistenceCreator;

@Entity
@Table(name = "student")
public class StudentEntity {
    @Id
    private final Integer id;
    private final String name;

    protected StudentEntity() {
        this.id = 0;
        this.name = "";
    }
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
