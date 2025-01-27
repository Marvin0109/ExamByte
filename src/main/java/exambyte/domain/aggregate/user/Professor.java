package exambyte.domain.aggregate.user;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.annotation.PersistenceCreator;


public class Professor implements Person {

    private final Long id;
    private final String name;

    private Professor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Factory Methode
    public static Professor of(Long id, String name) {
        return new Professor(id, name);
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}