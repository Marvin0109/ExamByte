package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("correct_answers")
public class KorrekteAntwortenEntity {

    @Id
    private final Long id;

    @Column("fach_id")
    private final UUID fachID;

    @Column("frage_id")
    private final UUID frageFachID;

    @Column("richtige_antwort")
    private final String richtigeAntwort;

    public KorrekteAntwortenEntity(Long id, UUID fachID, UUID frageFachID, List<String> richtigeAntworten) {
        this.id = id;
        this.fachID = fachID != null ? fachID : UUID.randomUUID();
        this.frageFachID = frageFachID;
        this.richtigeAntwort = String.join(", ", richtigeAntworten);
    }

    public Long getId() {
        return id;
    }

    public UUID getFachID() {
        return fachID;
    }

    public UUID getFrageFachID() {
        return frageFachID;
    }

    public String getRichtigeAntwort() {
        return richtigeAntwort;
    }
}
