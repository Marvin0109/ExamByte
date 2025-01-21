package exambyte.domain.aggregate.user;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.annotation.PersistenceCreator;


public class Professor implements Person {

    private final Integer id;
    private final String name;

    private Professor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Factory Methode
    public static Professor of(Integer id, String name) {
        return new Professor(id, name);
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}