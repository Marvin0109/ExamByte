package exambyte.application.dto;

import java.util.UUID;

public class KorrekteAntwortenDTO {

    private final Long id;
    private final UUID fachID;
    private final String antworten;
    private UUID frageFachID;

    public KorrekteAntwortenDTO(Long id, UUID fachID, UUID frageFachID, String antworten) {
        this.id = id;
        this.fachID = fachID;
        this.antworten = antworten;
        this.frageFachID = frageFachID;
    }

    public void setFrageFachID(UUID frageFachID) {
        this.frageFachID = frageFachID;
    }

    public UUID getFrageFachID() {
        return frageFachID;
    }

    public UUID getFachID() {
        return fachID;
    }

    public String getAntworten() {
        return antworten;
    }

    public Long getId() {
        return id;
    }
}
