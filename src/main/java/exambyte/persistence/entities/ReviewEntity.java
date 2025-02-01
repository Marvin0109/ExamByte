package exambyte.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("review")
public class ReviewEntity {

    @Id
    private final Long id;

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

    public ReviewEntity(Long id, UUID fachId, UUID antwortFachId, UUID korrektorFachId, String bewertung, int punkte) {
        this.id = id;
        this.fachId = fachId != null ? fachId : UUID.randomUUID();
        this.bewertung = bewertung;
        this.punkte = punkte;
        this.antwortFachId = antwortFachId;
        this.korrektorFachId = korrektorFachId;
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
}
