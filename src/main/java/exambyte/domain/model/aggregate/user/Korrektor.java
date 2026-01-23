package exambyte.domain.model.aggregate.user;

import java.util.UUID;

public class Korrektor implements Person {

    private final UUID fachId;
    private final String name;

    private Korrektor(UUID fachId, String name) {
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

    public static class KorrektorBuilder {
        private UUID fachId;
        private String name;

        public KorrektorBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public KorrektorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Korrektor build() {
            return new Korrektor(fachId, name);
        }
    }
}
