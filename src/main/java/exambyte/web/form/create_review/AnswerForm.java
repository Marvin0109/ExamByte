package exambyte.web.form.create_review;

import java.util.UUID;

public class AnswerForm {

    private String questionText;
    private double points;
    private String answer;
    private UUID answerId;

    public  String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public double getQuestionPoints() {
        return points;
    }

    public void setQuestionPoints(double points) {
        this.points = points;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }
}
