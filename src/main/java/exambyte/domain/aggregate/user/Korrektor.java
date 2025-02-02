package exambyte.domain.aggregate.user;

import java.util.UUID;

public class Korrektor implements Person {

    private final Long id;
    private final UUID fachId;
    private final String name;

    private Korrektor(Long id, UUID fachId, String name) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
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

    public static class KorrektorBuilder {
        private Long id;
        private UUID fachId;
        private String name;

        public KorrektorBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public KorrektorBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public KorrektorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Korrektor build() {
            return new Korrektor(id, fachId, name);
        }
    }
}
