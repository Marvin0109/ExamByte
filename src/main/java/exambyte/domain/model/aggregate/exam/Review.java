package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class Review {

    private final UUID fachId;
    private final UUID antwortFachId;
    private final UUID korrektorId;
    private final String bewertung;
    private final int punkte;

    private Review(UUID fachId, UUID antwortFachId, UUID korrektorId, String bewertung, int punkte) {
        this.fachId = fachId;
        this.antwortFachId = antwortFachId;
        this.korrektorId = korrektorId;
        this.bewertung = bewertung;
        this.punkte = punkte;
    }

    public UUID getFachId() {
        return fachId;
    }

    public UUID getAntwortFachId() {
        return antwortFachId;
    }

    public UUID getKorrektorFachId() {
        return korrektorId;
    }

    public String getBewertung() {
        return bewertung;
    }

    public int getPunkte() {
        return punkte;
    }

    public static class ReviewBuilder {
        private UUID fachId;
        private UUID antwortFachId;
        private UUID korrektorId;
        private String bewertung;
        private int punkte;

        public ReviewBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ReviewBuilder antwortFachId(UUID antwortFachId) {
            this.antwortFachId = antwortFachId;
            return this;
        }

        public ReviewBuilder korrektorFachId(UUID korrektorId) {
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
            return new Review(fachId, antwortFachId, korrektorId, bewertung, punkte);
        }
    }
}
