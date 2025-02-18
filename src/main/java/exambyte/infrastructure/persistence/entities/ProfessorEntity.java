package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("professor")
public class ProfessorEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private final String name;

    private ProfessorEntity(Long id, UUID fachId, String name) {
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

    public String getName() {
        return name;
    }

    public static class ProfessorEntityBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public ProfessorEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProfessorEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ProfessorEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProfessorEntity build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
            }
            return new ProfessorEntity(id, fachId, name);
        }
    }
}

