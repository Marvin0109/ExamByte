package exambyte.infrastructure.persistence.entities;

import exambyte.infrastructure.persistence.common.QuestionTypeEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("frage")
public class FrageEntity {
    
    @Id
    private UUID id;
    
    @Column("frage_text")
    private String frageText;

    @Column("type")
    private final QuestionTypeEntity type;

    @Column("exam_id")
    private final UUID examId;

    @Column("max_punkte")
    private int maxPunkte;

    private FrageEntity(UUID id, String frageText, int maxPunkte, QuestionTypeEntity type, UUID examId) {
        this.id = id;
        this.frageText = frageText;
        this.maxPunkte = maxPunkte;
        this.type = type;
        this.examId = examId;
    }

    public UUID getId() {
        return id;
    }

    public String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }

    public QuestionTypeEntity getType() {
        return type;
    }

    public UUID getExamId() {
        return examId;
    }

    public static class FrageEntityBuilder {
        private UUID id;
        private String frageText;
        private int maxPunkte;
        private QuestionTypeEntity type;
        private UUID examId;

        public FrageEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public FrageEntityBuilder frageText(String frageText) {
            this.frageText = frageText;
            return this;
        }

        public FrageEntityBuilder maxPunkte(int maxPunkte) {
            this.maxPunkte = maxPunkte;
            return this;
        }

        public FrageEntityBuilder type(QuestionTypeEntity type) {
            this.type = type;
            return this;
        }

        public FrageEntityBuilder examId(UUID examId) {
            this.examId = examId;
            return this;
        }

        public FrageEntity build() {
            if (frageText == null || frageText.isBlank()) {
                throw new IllegalStateException("Fragetext fehlt");
            }
            if (maxPunkte <= 0) {
                throw new IllegalStateException("Punkte dürfen nicht 0 oder negativ sein");
            }
            if (examId == null) {
                throw new IllegalStateException("Exam-ID fehlt");
            }
            return new FrageEntity(id, frageText, maxPunkte, type, examId);
        }
    }
}
