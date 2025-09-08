package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class KorrekteAntworten {

    private final Long id;
    private final UUID fachId;
    private final String korrekteAntworten;
    private final String antwort_optionen;
    private final UUID frageFachId;

    private KorrekteAntworten(Long id, UUID fachId, UUID frageFachId, String korrekteAntworten, String antwort_optionen) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.frageFachId = frageFachId;
        this.korrekteAntworten = korrekteAntworten;
        this.antwort_optionen = antwort_optionen;
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

    public String getKorrekteAntworten() {
        return korrekteAntworten;
    }

    public String getAntwort_optionen() { return antwort_optionen; }

    public static class KorrekteAntwortenBuilder {
        private Long id;
        private UUID fachId;
        private UUID frageFachId;
        private String korrekteAntworten;
        private String antwort_optionen;

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

        public KorrekteAntwortenBuilder korrekteAntworten(String antworten) {
            this.korrekteAntworten = antworten;
            return this;
        }

        public KorrekteAntwortenBuilder antwort_optionen(String optionen) {
            this.antwort_optionen = optionen;
            return this;
        }

        public KorrekteAntworten build() {
            return new KorrekteAntworten(id, fachId, frageFachId, korrekteAntworten, antwort_optionen);
        }
    }
}
