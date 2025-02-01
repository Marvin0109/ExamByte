package exambyte.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("korrektor")
public class KorrektorEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private final String name;

    public KorrektorEntity(Long id, UUID fachId, String name) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.name = name;
        this.id = id;
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
