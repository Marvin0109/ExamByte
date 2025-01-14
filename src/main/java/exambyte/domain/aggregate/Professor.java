package exambyte.domain.aggregate;

import jakarta.persistence.Id;
import org.springframework.data.annotation.PersistenceCreator;


public class Professor implements Person {
    @Id
    private final Integer id;
    private final String name;

    private Professor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    @PersistenceCreator
    public static Professor of(Integer id, String name) {
        return new Professor(id, name);
    }
    public static Professor of(String name) {
        return new Professor(null, name);
    }
}