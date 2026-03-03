package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public class Professor implements Person {

    private final UUID id;
    private final String name;

    private Professor(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static class ProfessorBuilder {
        private UUID id;
        private String name;

        public ProfessorBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ProfessorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Professor build() {
            return new Professor(id, name);
        }
    }
}