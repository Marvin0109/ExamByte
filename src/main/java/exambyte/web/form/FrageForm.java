package exambyte.web.form;

import exambyte.web.form.helper.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class FrageForm {

    @NotBlank(message = "Der Titel darf nicht leer sein!")
    private String title;

    @NotBlank(message = "Die Aufgabenstellung darf nicht leer sein!")
    private String frageText;

    @Min(value = 1, message = "Punkte dürfen nicht negativ sein!")
    private int maxPunkte;

    @NotBlank(message = "Der Fragetyp muss ausgesucht werden!")
    private QuestionType type;

    // Falls SC oder MC, mögliche Antworten
    @Size(min = 3, message = "Es müssen mind. 3 Antwortmöglichkeiten gegeben sein!")
    private List<String> choices;

    // Richtige Antwort für SC
    @NotBlank(message = "Die Lösung muss gegeben sein!")
    private String correctAnswer;

    // MC, Liste korrekter Antworten
    @NotBlank(message = "Die Liste der richtigen Antworten darf nicht leer sein!")
    @Size(min = 2, message = "Es muss mindestens zwei richtige Antworten angegeben werden!")
    private List<String> correctAnswers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getMaxPunkte() {
        return maxPunkte;
    }

    public void setMaxPunkte(int maxPunkte) {
        this.maxPunkte = maxPunkte;
    }
}
