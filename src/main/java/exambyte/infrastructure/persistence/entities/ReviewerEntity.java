package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("reviewer")
public class ReviewerEntity {

    @Id
    private UUID id;

    @Column("name")
    private final String name;

    private ReviewerEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class ReviewerEntityBuilder {
        private UUID id;
        private String name;

        public ReviewerEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewerEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ReviewerEntity build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Name darf nicht leer sein");
            }
            return new ReviewerEntity(id, name);
        }
    }
}
