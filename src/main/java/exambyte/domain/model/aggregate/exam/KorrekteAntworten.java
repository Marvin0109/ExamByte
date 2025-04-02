package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class KorrekteAntworten {

    private final Long id;
    private final UUID fachId;
    private final String korrekteAntworten;
    private final UUID frageFachId;

    private KorrekteAntworten(Long id, UUID fachId, UUID frageFachId, String korrekteAntworten) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.frageFachId = frageFachId;
        this.korrekteAntworten = korrekteAntworten;
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

    public static class KorrekteAntwortenBuilder {
        private Long id;
        private UUID fachId;
        private UUID frageFachId;
        private String korrekteAntworten;

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

        public KorrekteAntworten build() {
            return new KorrekteAntworten(id, fachId, frageFachId, korrekteAntworten);
        }
    }
}
