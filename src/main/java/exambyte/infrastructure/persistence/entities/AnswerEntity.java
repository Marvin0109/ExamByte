package exambyte.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("answer")
public class AnswerEntity {

    @Id
    private UUID id;

    @Column("answer")
    private String answer;

    @Column("question_id")
    private final UUID questionId;

    @Column("student_id")
    private final UUID studentId;

    @Column("submit_time")
    private final LocalDateTime submitTime;

    private AnswerEntity(UUID id, String answer, UUID questionId, UUID studentId,
                         LocalDateTime submitTime) {
        this.id = id;
        this.answer = answer;
        this.questionId = questionId;
        this.studentId = studentId;
        this.submitTime = submitTime;
    }

    public UUID getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public static class AnswerEntityBuilder {
        private UUID id;
        private UUID questionId;
        private String answer;
        private UUID studentId;
        private LocalDateTime submitTime;

        public AnswerEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AnswerEntityBuilder questionId(UUID questionId) {
            this.questionId = questionId;
            return this;
        }

        public AnswerEntityBuilder studentId(UUID studentId) {
            this.studentId = studentId;
            return this;
        }

        public AnswerEntityBuilder answer(String answer) {
            this.answer = answer;
            return this;
        }

        public AnswerEntityBuilder submitTime(LocalDateTime submitTime) {
            this.submitTime = submitTime;
            return this;
        }

        public AnswerEntity build() {
            if (answer == null || answer.isBlank()) {
                throw new IllegalStateException("Student answer is missing");
            }
            if (questionId == null) {
                throw new IllegalStateException("Question id is missing");
            }
            if (studentId == null) {
                throw new IllegalStateException("Student id is missing");
            }
            return new AnswerEntity(id, answer, questionId, studentId, submitTime);
        }
    }
}
