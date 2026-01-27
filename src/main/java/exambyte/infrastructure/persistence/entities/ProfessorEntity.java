package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("professor")
public class ProfessorEntity {

    @Id
    private Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private final String name;

    public ProfessorEntity(UUID fachId, String name) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.name = name;
    }

    public UUID getFachId() {
        return fachId;
    }

    public String getName() {
        return name;
    }

    public static class ProfessorEntityBuilder {
        private UUID fachId;
        private String name;

        public ProfessorEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ProfessorEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProfessorEntity build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Name darf nicht leer sein");
            }
            return new ProfessorEntity(fachId, name);
        }
    }
}

