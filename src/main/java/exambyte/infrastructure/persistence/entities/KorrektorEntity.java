package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("korrektor")
public class KorrektorEntity {

    @Id
    private UUID id;

    @Column("name")
    private final String name;

    private KorrektorEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class KorrektorEntityBuilder {
        private UUID id;
        private String name;

        public KorrektorEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public KorrektorEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public KorrektorEntity build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Name darf nicht leer sein");
            }
            return new KorrektorEntity(id, name);
        }
    }
}
