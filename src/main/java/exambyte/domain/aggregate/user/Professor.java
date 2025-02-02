package exambyte.domain.aggregate.user;

import java.util.UUID;

public class Professor implements Person {

    private final Long id;
    private final UUID fachId;
    private final String name;

    private Professor(Long id, UUID fachId, String name) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID uuid() {
        return fachId;
    }
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }

    public static class ProfessorBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public ProfessorBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProfessorBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ProfessorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Professor build() {
            return new Professor(id, fachId, name);
        }
    }
}