package exambyte.web.form.show_review;

import exambyte.web.common.QuestionTypeWeb;

public record ReviewComponent(
        String frageStellung,
        int maxPunkte,
        QuestionTypeWeb questionType,
        String antwortOptionen,
        String studentAntworten,
        String loesungen,
        String bewertung,
        int punkteVergeben
) {}
