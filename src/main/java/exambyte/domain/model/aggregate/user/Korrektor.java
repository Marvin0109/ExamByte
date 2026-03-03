package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public class Korrektor implements Person {

    private final UUID id;
    private final String name;

    private Korrektor(UUID id, String name) {
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

    public static class KorrektorBuilder {
        private UUID id;
        private String name;

        public KorrektorBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public KorrektorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Korrektor build() {
            return new Korrektor(id, name);
        }
    }
}
