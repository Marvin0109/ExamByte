package exambyte.domain.aggregate.exam;

import java.util.UUID;

public class Review {

    private final Long id;
    private final UUID fachId;
    private final UUID antwortFachId;
    private final UUID korrektorId;
    private String bewertung;
    private int punkte;

    private Review(Long id, UUID fachId, UUID antwortFachId, UUID korrektorId, String bewertung, int punkte) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.antwortFachId = antwortFachId;
        this.korrektorId = korrektorId;
        this.bewertung = bewertung;
        this.punkte = punkte;
    }

    public Long getId() {
        return id;
    }

    public UUID getFachId() {
        return fachId;
    }

    public UUID getAntwortFachId() {
        return antwortFachId;
    }

    public UUID getKorrektorId() {
        return korrektorId;
    }

    public String getBewertung() {
        return bewertung;
    }

    public void setBewertung(String bewertung) {
        this.bewertung = bewertung;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public static class ReviewBuilder {
        private Long id;
        private UUID fachId;
        private UUID antwortFachId;
        private UUID korrektorId;
        private String bewertung;
        private int punkte;

        public ReviewBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReviewBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ReviewBuilder antwortFachId(UUID antwortFachId) {
            this.antwortFachId = antwortFachId;
            return this;
        }

        public ReviewBuilder korrektorId(UUID korrektorId) {
            this.korrektorId = korrektorId;
            return this;
        }

        public ReviewBuilder bewertung(String bewertung) {
            this.bewertung = bewertung;
            return this;
        }

        public ReviewBuilder punkte(int punkte) {
            this.punkte = punkte;
            return this;
        }

        public Review build() {
            return new Review(id, fachId, antwortFachId, korrektorId, bewertung, punkte);
        }
    }
}
