package exambyte.domain.model.aggregate.exam;

import java.util.UUID;

public class CorrectAnswers {

    private final UUID id;
    private final String solution;
    private final String choices;
    private final UUID frageId;

    private CorrectAnswers(UUID id, UUID frageId, String solution, String choices) {
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

    public static class CorrectAnswersBuilder {
        private UUID id;
        private UUID frageId;
        private String solution;
        private String choices;

        public CorrectAnswersBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CorrectAnswersBuilder frageId(UUID frageId) {
            this.frageId = frageId;
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
            return new CorrectAnswers(id, frageId, solution, choices);
        }
    }
}
