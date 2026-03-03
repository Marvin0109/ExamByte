package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class Review {

    private final UUID id;
    private final UUID antwortId;
    private final UUID korrektorId;
    private final String bewertung;
    private final int punkte;

    private Review(UUID id, UUID antwortId, UUID korrektorId, String bewertung, int punkte) {
        this.id = id;
        this.antwortId = antwortId;
        this.korrektorId = korrektorId;
        this.bewertung = bewertung;
        this.punkte = punkte;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAntwortId() {
        return antwortId;
    }

    public UUID getKorrektorId() {
        return korrektorId;
    }

    public String getBewertung() {
        return bewertung;
    }

    public int getPunkte() {
        return punkte;
    }

    public static class ReviewBuilder {
        private UUID id;
        private UUID antwortId;
        private UUID korrektorId;
        private String bewertung;
        private int punkte;

        public ReviewBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReviewBuilder antwortId(UUID antwortId) {
            this.antwortId = antwortId;
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
            return new Review(id, antwortId, korrektorId, bewertung, punkte);
        }
    }
}
