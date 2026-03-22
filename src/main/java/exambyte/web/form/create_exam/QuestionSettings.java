package exambyte.web.form.create_exam;

import exambyte.web.common.QuestionTypeWeb;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QuestionSettings {

    @NotNull(message = "MC-Anzahl darf nicht leer sein")
    @Min(value = 1, message = "MC-Anzahl muss größer 0 sein")
    @Max(value = 10, message = "MC-Anzahl darf höchstens 10 sein")
    private Integer mcCount;

    @NotNull(message = "SC-Anzahl darf nicht leer sein")
    @Min(value = 1, message = "SC-Anzahl muss größer 0 sein")
    @Max(value = 10, message = "SC-Anzahl darf höchstens 10 sein")
    private Integer scCount;

    @NotNull(message = "Freitext-Anzahl darf nicht leer sein")
    @Min(value = 1, message = "Freitext-Anzahl muss größer 0 sein")
    @Max(value = 10, message = "Freitext-Anzahl darf höchstens 10 sein")
    private Integer freeResponseCount;

    private List<QuestionTypeWeb> questionTypeList;

    public Integer getMcCount() {
        return mcCount;
    }

    public void setMcCount(Integer mcCount) {
        this.mcCount = mcCount;
    }

    public Integer getScCount() {
        return scCount;
    }

    public void setScCount(Integer scCount) {
        this.scCount = scCount;
    }

    public Integer getFreeResponseCount() {
        return freeResponseCount;
    }

    public void setFreeResponseCount(Integer freeResponseCount) {
        this.freeResponseCount = freeResponseCount;
    }

    public List<QuestionTypeWeb> getQuestionTypeList() {
        return questionTypeList;
    }

    public void setQuestionTypeList(List<QuestionTypeWeb> questionTypeList) {
        this.questionTypeList = questionTypeList;
    }
}
