package exambyte.domain.model.exam;

import java.util.UUID;

public class CorrectAnswers {

    private final UUID id;
    private final String solution;
    private final String choices;
    private final UUID questionId;

    private CorrectAnswers(UUID id, UUID questionId, String solution, String choices) {
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

    public static class CorrectAnswersBuilder {
        private UUID id;
        private UUID questionId;
        private String solution;
        private String choices;

        public CorrectAnswersBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CorrectAnswersBuilder questionId(UUID questionId) {
            this.questionId = questionId;
            return this;
        }

        public CorrectAnswersBuilder solution(String solution) {
            this.solution = solution;
            return this;
        }

        public CorrectAnswersBuilder choices(String choices) {
            this.choices = choices;
            return this;
        }

        public CorrectAnswers build() {
            return new CorrectAnswers(id, questionId, solution, choices);
        }
    }
}
