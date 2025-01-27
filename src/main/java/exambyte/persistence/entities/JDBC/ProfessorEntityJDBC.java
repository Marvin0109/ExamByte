package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("professor")
public class ProfessorEntityJDBC {

    @Id
    private final Long id;
    private final String name;

    public ProfessorEntityJDBC() {
        this(null, "");
    }

    public ProfessorEntityJDBC(Long id, String name) {
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

