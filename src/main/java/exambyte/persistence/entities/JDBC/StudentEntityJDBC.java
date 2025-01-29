package exambyte.persistence.entities.JDBC;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("student")
public class StudentEntityJDBC {

    @Id
    private Long id;

    @Column("name")
    private String name;

    public StudentEntityJDBC() {}

    public StudentEntityJDBC(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
