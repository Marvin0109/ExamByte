package exambyte.domain.model.exam;

import java.time.LocalDateTime;
import java.util.UUID;

public class Answer {

    private final UUID id;
    private final String studentAnswer;
    private final UUID questionId;
    private final UUID studentId;
    private final LocalDateTime submitTime;

    private Answer(UUID id, String answer, UUID questionId, UUID studentId,
                   LocalDateTime submitTime) {
        this.id = id;
        this.studentAnswer = answer;
        this.questionId = questionId;
        this.studentId = studentId;
        this.submitTime = submitTime;
    }

    public String getAnswer() {
        return studentAnswer;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public UUID getStudentUUID() {
        return studentId;
    }
    public UUID getId() { return id; }

    public LocalDateTime getSubmitTime() { return submitTime; }

    public static class AnswerBuilder {
        private UUID id;
        private String answer;
        private UUID questionId;
        private UUID studentId;
        private LocalDateTime submitTime;

        public AnswerBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AnswerBuilder answer(String answer) {
            this.answer = answer;
            return this;
        }

        public AnswerBuilder questionId(UUID questionId) {
            this.questionId = questionId;
            return this;
        }

        public AnswerBuilder studentId(UUID studentId) {
            this.studentId = studentId;
            return this;
        }

        public AnswerBuilder submitTime(LocalDateTime submitTime) {
            this.submitTime = submitTime;
            return this;
        }

        public Answer build() {
            return new Answer(id, answer, questionId, studentId, submitTime);
        }
    }
}
