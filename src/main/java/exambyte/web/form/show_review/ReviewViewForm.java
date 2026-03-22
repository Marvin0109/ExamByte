package exambyte.web.form.show_review;

import java.util.List;

public record ReviewViewForm(
        String examTitle,
        String authorName,
        double reviewPoints,
        double maxPoints,
        List<ReviewAggregateDTO> components) {}
