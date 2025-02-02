package exambyte.domain.aggregate.user;

import java.util.UUID;

public class Student implements Person {

    private final Long id;
    private final UUID fachId;
    private final String name;

    private Student(Long id, UUID fachId, String name) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID uuid() { return fachId; }
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }

    public static class StudentBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public StudentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Student build() {
            return new Student(id, fachId, name);
        }
    }
}