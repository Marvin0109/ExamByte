package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class KorrekteAntworten {

    private final Long id;
    private final UUID fachId;
    private final String loesungen;
    private final String antwortOptionen;
    private final UUID frageFachId;

    private KorrekteAntworten(Long id, UUID fachId, UUID frageFachId, String loesungen, String antwortOptionen) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.frageFachId = frageFachId;
        this.loesungen = loesungen;
        this.antwortOptionen = antwortOptionen;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public UUID getFrageFachId() {
        return frageFachId;
    }

    public String getLoesungen() {
        return loesungen;
    }

    public String getAntwortOptionen() { return antwortOptionen; }

    public static class KorrekteAntwortenBuilder {
        private Long id;
        private UUID fachId;
        private UUID frageFachId;
        private String loesungen;
        private String antwortOptionen;

        public KorrekteAntwortenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public KorrekteAntwortenBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public KorrekteAntwortenBuilder frageFachId(UUID frageFachId) {
            this.frageFachId = frageFachId;
            return this;
        }

        public KorrekteAntwortenBuilder loesungen(String loesungen) {
            this.loesungen = loesungen;
            return this;
        }

        public KorrekteAntwortenBuilder antwortOptionen(String optionen) {
            this.antwortOptionen = optionen;
            return this;
        }

        public KorrekteAntworten build() {
            return new KorrekteAntworten(id, fachId, frageFachId, loesungen, antwortOptionen);
        }
    }
}
