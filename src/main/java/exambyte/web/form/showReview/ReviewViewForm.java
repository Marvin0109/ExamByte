package exambyte.web.form.showReview;

import java.util.List;

public record ReviewViewForm(
        String examTitle,
        String authorName,
        int erreichtePunkte,
        int maxPunkte,
        List<ReviewComponent> components
) {}
