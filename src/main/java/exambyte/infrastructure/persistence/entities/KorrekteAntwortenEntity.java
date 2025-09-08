package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    @Column("antwort_optionen")
    private final String antwort_optionen;

    public KorrekteAntwortenEntity(Long id, UUID fachID, UUID frageFachID, String richtigeAntwort, String antwort_optionen) {
        this.id = id;
        this.fachID = fachID != null ? fachID : UUID.randomUUID();
        this.frageFachID = frageFachID;
        this.richtigeAntwort = richtigeAntwort;
        this.antwort_optionen = antwort_optionen;
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

    public String getAntwort_optionen() { return antwort_optionen; }
}
