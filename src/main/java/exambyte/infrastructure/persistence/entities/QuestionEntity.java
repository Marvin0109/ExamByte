package exambyte.infrastructure.persistence.entities;

import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("question")
public class QuestionEntity {
    
    @Id
    private UUID id;
    
    @Column("text")
    private String text;

    @Column("type")
    private final QuestionTypeEntity type;

    @Column("exam_id")
    private final UUID examId;

    @Column("points")
    private int points;

    private QuestionEntity(UUID id, String text, int points, QuestionTypeEntity type, UUID examId) {
        this.id = id;
        this.text = text;
        this.points = points;
        this.type = type;
        this.examId = examId;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public QuestionTypeEntity getType() {
        return type;
    }

    public UUID getExamId() {
        return examId;
    }

    public static class QuestionEntityBuilder {
        private UUID id;
        private String text;
        private int points;
        private QuestionTypeEntity type;
        private UUID examId;

        public QuestionEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public QuestionEntityBuilder text(String text) {
            this.text = text;
            return this;
        }

        public QuestionEntityBuilder points(int points) {
            this.points = points;
            return this;
        }

        public QuestionEntityBuilder type(QuestionTypeEntity type) {
            this.type = type;
            return this;
        }

        public QuestionEntityBuilder examId(UUID examId) {
            this.examId = examId;
            return this;
        }

        public QuestionEntity build() {
            if (text == null || text.isBlank()) {
                throw new IllegalStateException("Fragetext fehlt");
            }
            if (points <= 0) {
                throw new IllegalStateException("Punkte dürfen nicht 0 oder negativ sein");
            }
            if (examId == null) {
                throw new IllegalStateException("Exam-ID fehlt");
            }
            return new QuestionEntity(id, text, points, type, examId);
        }
    }
}
