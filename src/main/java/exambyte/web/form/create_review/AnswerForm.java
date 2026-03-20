package exambyte.web.form.create_review;

import java.util.UUID;

public class AnswerForm {

    private String frageText;
    private double maxPunkte;
    private String answer;
    private UUID answerId;

    public  String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public double getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(double maxPunkte) {
        this.maxPunkte = maxPunkte;
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
