package exambyte.web.form;

import java.util.List;

public class QuestionData {
    private int index;
    private String questionText;
    private String type;
    private int punkte;
    private List<String> choices;
    private String correctAnswer;
    private List<String> correctAnswers;

    public String getQuestionText() {
        return questionText;
    }

    public String getType() {
        return type;
    }

    public int getPunkte() {
        return punkte;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public int getIndex() {
        return index;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
