package exambyte.web.form.submit_answers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public class SubmitForm {

    @Valid
    private Map<String, @NotEmpty List<@NotBlank String>> answers;

    public Map<String, List<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, List<String>> answers) {
        this.answers = answers;
    }
}
