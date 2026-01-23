package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public class Student implements Person {

    private final UUID fachId;
    private final String name;

    private Student(UUID fachId, String name) {
        this.fachId = fachId;
        this.name = name;
    }

    @Override
    public UUID uuid() { return fachId; }

    @Override
    public String getName() {
        return name;
    }

    public static class StudentBuilder {
        private UUID fachId;
        private String name;

        public StudentBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Student build() {
            return new Student(fachId, name);
        }
    }
}