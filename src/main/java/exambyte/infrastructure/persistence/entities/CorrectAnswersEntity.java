package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("correct_answers")
public class CorrectAnswersEntity {

    @Id
    private UUID id;

    @Column("frage_id")
    private final UUID frageId;

    @Column("solution")
    private final String solution;

    @Column("choices")
    private final String choices;

    private CorrectAnswersEntity(UUID id, UUID frageId, String solution, String choices) {
        this.id = id;
        this.frageId = frageId;
        this.solution = solution;
        this.choices = choices;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFrageId() {
        return frageId;
    }

    public String getSolution() {
        return solution;
    }

    public String getChoices() { return choices; }

    public static class CorrectAnswersEntityBuilder {
        private UUID id;
        private UUID frageId;
        private String solution;
        private String choices;

        public CorrectAnswersEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CorrectAnswersEntityBuilder frageId(UUID frageId) {
            this.frageId = frageId;
            return this;
        }

        public CorrectAnswersEntityBuilder solution(String solution) {
            this.solution = solution;
            return this;
        }

        public CorrectAnswersEntityBuilder choices(String choices) {
            this.choices = choices;
            return this;
        }

        public CorrectAnswersEntity build() {
            if (frageId == null) {
                throw new IllegalStateException("Frage-ID fehlt");
            }
            checkStringField(solution, "Lösungen fehlen");
            checkStringField(choices, "Antwort Optionen fehlen");
            return new CorrectAnswersEntity(id, frageId, solution, choices);
        }

        private static void checkStringField(String field, String message) {
            if (field == null || field.isBlank()) {
                throw new IllegalStateException(message);
            }
        }
    }
}
