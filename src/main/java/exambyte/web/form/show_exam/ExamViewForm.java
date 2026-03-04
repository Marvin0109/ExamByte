package exambyte.web.form.show_exam;

import java.util.List;

public record ExamViewForm(
        String examTitle,
        String authorName,
        double maxPunkte,
        List<ExamAggregateDTO> questions) {}
