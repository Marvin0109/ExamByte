package exambyte.web.form.submit_answers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public class SubmitForm {

    @Valid
    private Map<String, @NotEmpty(message = "Bitte bearbeiten Sie die Aufgaben.") List<
            @NotBlank(message = "Leerzeichen nicht erlaubt.")
            @Size(max = 5000, message = "Maximale Zeichenlänge ist überschritten worden.") String>>
        answers;

    public Map<String, List<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, List<String>> answers) {
        this.answers = answers;
    }
}
