package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("professor")
public class ProfessorEntityJDBC {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private final String name;

    public ProfessorEntityJDBC(Long id, UUID fachId, String name) {
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

    public String getName() {
        return name;
    }
}

