package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public class Professor implements Person {

    private final UUID fachId;
    private final String name;

    private Professor(UUID fachId, String name) {
        this.fachId = fachId;
        this.name = name;
    }

    @Override
    public UUID uuid() {
        return fachId;
    }

    @Override
    public String getName() {
        return name;
    }

    public static class ProfessorBuilder {
        private UUID fachId;
        private String name;

        public ProfessorBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ProfessorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Professor build() {
            return new Professor(fachId, name);
        }
    }
}