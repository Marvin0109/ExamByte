package exambyte.domain.aggregate;

import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.annotation.PersistenceCreator;

public class Student implements Person {
    @Id
    private final Integer id;
    private final String name;

    private Student(Integer id, String name) {
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
    public static Student of(Integer id, String name) {
        return new Student(id, name);
    }
    public static Student of(String name) {
        return new Student(null, name);
    }
}