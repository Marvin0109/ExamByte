package exambyte.web.form;

import java.util.List;

public record ReviewViewForm(
        String examTitle,
        String authorName,
        int erreichtePunkte,
        int maxPunkte,
        List<ReviewComponent> components
) {}
