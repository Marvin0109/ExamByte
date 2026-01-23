package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class KorrekteAntworten {

    private final UUID fachId;
    private final String loesungen;
    private final String antwortOptionen;
    private final UUID frageFachId;

    private KorrekteAntworten(UUID fachId, UUID frageFachId, String loesungen, String antwortOptionen) {
        this.fachId = fachId;
        this.frageFachId = frageFachId;
        this.loesungen = loesungen;
        this.antwortOptionen = antwortOptionen;
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
        private UUID fachId;
        private UUID frageFachId;
        private String loesungen;
        private String antwortOptionen;

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
            return new KorrekteAntworten(fachId, frageFachId, loesungen, antwortOptionen);
        }
    }
}
