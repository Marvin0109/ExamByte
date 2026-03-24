package exambyte.domain.model.exam;

import exambyte.domain.model.enums.QuestionType;

import java.util.UUID;

public class Question {

    private final UUID id;
    private final String text;
    private final double points;
    private final QuestionType type;
    private final UUID examId;

    private Question(UUID id, String text, double points, QuestionType type, UUID examId) {
        this.id = id;
        this.text = text;
        this.points = points;
        this.type = type;
        this.examId = examId;
    }

    public String getText() {
        return text;
    }

    public UUID getId() {
        return id;
    }

    public UUID getExamId() {
        return examId;
    }

    public double getPoints() {
        return points;
    }

    public QuestionType getType() { return type; }

    public static class FrageBuilder {
        private UUID id;
        private String text;
        private double points;
        private QuestionType type;
        private UUID examId;

        public FrageBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public FrageBuilder text(String text) {
            this.text = text;
            return this;
        }

        public FrageBuilder points(double points) {
            this.points = points;
            return this;
        }

        public FrageBuilder type(QuestionType type) {
            this.type = type;
            return this;
        }

        public FrageBuilder examId(UUID examId) {
            this.examId = examId;
            return this;
        }

        public Question build() {
            return new Question(id, text, points, type, examId);
        }
    }
}
