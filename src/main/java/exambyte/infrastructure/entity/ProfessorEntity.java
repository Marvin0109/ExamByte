package exambyte.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("professor")
public class ProfessorEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    public ProfessorEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ProfessorEntityBuilder {
        private UUID id;
        private String name;

        public ProfessorEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ProfessorEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProfessorEntity build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Name is missing");
            }
            return new ProfessorEntity(id, name);
        }
    }
}

