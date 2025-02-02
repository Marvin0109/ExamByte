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

    private KorrektorEntity(Long id, UUID fachId, String name) {
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

    public static class KorrektorEntityBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public KorrektorEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public KorrektorEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public KorrektorEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public KorrektorEntity build() {
            if (name == null || name.isEmpty() ) {
                throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
            }
            return new KorrektorEntity(id, fachId, name);
        }
    }
}
