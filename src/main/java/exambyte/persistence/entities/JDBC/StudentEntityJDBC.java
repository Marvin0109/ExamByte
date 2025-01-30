package exambyte.persistence.entities.JDBC;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("student")
public class StudentEntityJDBC {

    @Id
    private Long id;

    @Column("fach_id")
    private UUID fachId;

    @Column("name")
    private String name;

    public StudentEntityJDBC() {}

    public StudentEntityJDBC(Long id, UUID fachId, String name) {
        this.fachId = fachId;
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
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
