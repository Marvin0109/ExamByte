package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class KorrekteAntworten {

    private final UUID id;
    private final String loesungen;
    private final String antwortOptionen;
    private final UUID frageId;

    private KorrekteAntworten(UUID id, UUID frageId, String loesungen, String antwortOptionen) {
        this.id = id;
        this.frageId = frageId;
        this.loesungen = loesungen;
        this.antwortOptionen = antwortOptionen;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFrageId() {
        return frageId;
    }

    public String getLoesungen() {
        return loesungen;
    }

    public String getAntwortOptionen() { return antwortOptionen; }

    public static class KorrekteAntwortenBuilder {
        private UUID id;
        private UUID frageId;
        private String loesungen;
        private String antwortOptionen;

        public KorrekteAntwortenBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public KorrekteAntwortenBuilder frageId(UUID frageId) {
            this.frageId = frageId;
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
            return new KorrekteAntworten(id, frageId, loesungen, antwortOptionen);
        }
    }
}
