package exambyte.service.dto;

import java.util.UUID;

public class ReviewDTO {

    private final Long id;
    private final UUID fachId;
    private final UUID antwortFachId;
    private final UUID korrektorId;
    private String bewertung;
    private int punkte;

    public ReviewDTO(Long id, UUID fachId, UUID antwortFachId, UUID korrektorId, String bewertung, int punkte) {
        this.id = id;
        this.fachId = fachId;
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
}
