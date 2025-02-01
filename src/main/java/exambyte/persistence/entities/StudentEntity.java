package exambyte.persistence.entities.JDBC;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("student")
public class StudentEntityJDBC {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private String name;

    public StudentEntityJDBC(Long id, UUID fachId, String name) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
