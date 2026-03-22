package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("correct_answers")
public class CorrectAnswersEntity {

    @Id
    private UUID id;

    @Column("question_id")
    private final UUID questionId;

    @Column("solution")
    private final String solution;

    @Column("choices")
    private final String choices;

    private CorrectAnswersEntity(UUID id, UUID questionId, String solution, String choices) {
        this.id = id;
        this.questionId = questionId;
        this.solution = solution;
        this.choices = choices;
    }

    public UUID getId() {
        return id;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public String getSolution() {
        return solution;
    }

    public String getChoices() { return choices; }

    public static class CorrectAnswersEntityBuilder {
        private UUID id;
        private UUID questionId;
        private String solution;
        private String choices;

        public CorrectAnswersEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CorrectAnswersEntityBuilder questionId(UUID questionId) {
            this.questionId = questionId;
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
            if (questionId == null) {
                throw new IllegalStateException("Question id is missing");
            }
            checkStringField(solution, "Solutions are missing");
            checkStringField(choices, "Choices are missing");
            return new CorrectAnswersEntity(id, questionId, solution, choices);
        }

        private static void checkStringField(String field, String message) {
            if (field == null || field.isBlank()) {
                throw new IllegalStateException(message);
            }
        }
    }
}
