package exambyte.infrastructure.persistence.entities;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("student")
public class StudentEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("name")
    private String name;

    private StudentEntity(Long id, UUID fachId, String name) {
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

    public static class StudentEntityBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public StudentEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public StudentEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentEntity build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name darf nicht null oder leer sein.");
            }
            return new StudentEntity(id, fachId, name);
        }
    }
}
