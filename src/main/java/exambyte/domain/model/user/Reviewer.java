package exambyte.domain.model.user;

import java.util.UUID;

public class Reviewer implements Person {

    private final UUID id;
    private final String name;

    private Reviewer(UUID id, String name) {
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

    public static class ReviewerBuilder {
        private UUID id;
        private String name;

        public ReviewerBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Reviewer build() {
            return new Reviewer(id, name);
        }
    }
}
