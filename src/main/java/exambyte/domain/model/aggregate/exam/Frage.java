package exambyte.domain.model.aggregate.exam;

import exambyte.domain.model.common.QuestionType;

import java.util.UUID;

public class Frage {

    private final UUID id;
    private final String frageText;
    private final int maxPunkte;
    private final QuestionType type;
    private final UUID examId;

    private Frage(UUID id, String frageText, int maxPunkte, QuestionType type, UUID examId) {
        this.id = id;
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.type = type;
        this.examId = examId;
    }

    public String getFrageText() {
        return frageText;
    }

    public UUID getId() {
        return id;
    }

    public UUID getExamId() {
        return examId;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public QuestionType getType() { return type; }

    public static class FrageBuilder {
        private UUID id;
        private String frageText;
        private int maxPunkte;
        private QuestionType type;
        private UUID examId;

        public FrageBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public FrageBuilder frageText(String frageText) {
            this.frageText = frageText;
            return this;
        }

        public FrageBuilder maxPunkte(int maxPunkte) {
            this.maxPunkte = maxPunkte;
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

        public Frage build() {
            return new Frage(id, frageText, maxPunkte, type, examId);
        }
    }
}
