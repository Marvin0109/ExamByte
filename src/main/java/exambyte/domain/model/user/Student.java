package exambyte.domain.model.user;

import java.util.UUID;

public class Student implements Person {

    private final UUID id;
    private final String name;

    private Student(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID id() { return id; }

    @Override
    public String getName() {
        return name;
    }

    public static class StudentBuilder {
        private UUID id;
        private String name;

        public StudentBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Student build() {
            return new Student(id, name);
        }
    }
}