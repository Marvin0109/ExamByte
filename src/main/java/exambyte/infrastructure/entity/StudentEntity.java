package exambyte.infrastructure.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("student")
public class StudentEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    private StudentEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class StudentEntityBuilder {
        private UUID id;
        private String name;

        public StudentEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public StudentEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentEntity build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Name is missing");
            }
            return new StudentEntity(id, name);
        }
    }
}
