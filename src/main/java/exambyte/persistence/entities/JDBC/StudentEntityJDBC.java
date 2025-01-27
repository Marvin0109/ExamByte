package exambyte.persistence.entities.JDBC;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("student")
public class StudentEntityJDBC {

    @Id
    private final Long id;

    @Column("name")
    private final String name;

    public StudentEntityJDBC() {
        this(null, "");
    }

    public StudentEntityJDBC(Long id, String name) {
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
