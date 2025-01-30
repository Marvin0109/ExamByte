package exambyte.persistence.entities.JDBC;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("professor")
public class ProfessorEntityJDBC {

    @Id
    private final Long id;
    private final UUID uuid;
    private final String name;

    public ProfessorEntityJDBC() {
        this(null, UUID.randomUUID(), "");
    }

    public ProfessorEntityJDBC(Long id, UUID uuid, String name) {
        this.uuid = uuid;
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}

