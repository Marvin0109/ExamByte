package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("review")
public class ReviewEntity {

    @Id
    private Long id;

    @Column("fach_id")
    private final UUID fachId;

    @Column("antwort_fach_id")
    private final UUID antwortFachId;

    @Column("korrektor_fach_id")
    private final UUID korrektorFachId;

    @Column("bewertung")
    private String bewertung;

    @Column("punkte")
    private int punkte;

    private ReviewEntity(UUID fachId, UUID antwortFachId, UUID korrektorFachId, String bewertung, int punkte) {
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.bewertung = bewertung;
        this.punkte = punkte;
        this.antwortFachId = antwortFachId;
        this.korrektorFachId = korrektorFachId;
    }

    public UUID getFachId() {
        return fachId;
    }

    public UUID getAntwortFachId() {
        return antwortFachId;
    }

    public UUID getKorrektorFachId() {
        return korrektorFachId;
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

    public static class ReviewEntityBuilder {
        private UUID fachId;
        private String bewertung;
        private int punkte;
        private UUID antwortFachId;
        private UUID korrektorFachId;

        public ReviewEntityBuilder fachId(UUID fachId) {
            this.fachId = fachId;
            return this;
        }

        public ReviewEntityBuilder bewertung(String bewertung) {
            this.bewertung = bewertung;
            return this;
        }

        public ReviewEntityBuilder punkte(int punkte) {
            this.punkte = punkte;
            return this;
        }

        public ReviewEntityBuilder antwortFachId(UUID antwortFachId) {
            this.antwortFachId = antwortFachId;
            return this;
        }

        public ReviewEntityBuilder korrektorFachId(UUID korrektorFachId) {
            this.korrektorFachId = korrektorFachId;
            return this;
        }

        public ReviewEntity build() {
            checkFachID(antwortFachId, "Antwort-Fach-ID fehlt");
            checkFachID(korrektorFachId, "Korrektor-Fach-ID fehlt");
            if (bewertung == null || bewertung.isBlank()) {
                throw new IllegalStateException("Bewertung fehlt");
            }
            if (punkte < 0) {
                throw new IllegalStateException("Punkte dürfen nicht negativ sein");
            }
            return new ReviewEntity(fachId, antwortFachId, korrektorFachId, bewertung, punkte);
        }

        private static void checkFachID(UUID fachId, String message) {
            if (fachId == null) {
                throw new IllegalStateException(message);
            }
        }
    }
}
