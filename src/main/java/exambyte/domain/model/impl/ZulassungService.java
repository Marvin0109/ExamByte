package exambyte.domain.model.impl;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ZulassungService {

    public boolean berechneZulassung(List<Integer> punkteProExam, List<Integer> erreichtePunkteProExam) {
        List<Boolean> examBestanden = new ArrayList<>();

        for (int i = 0; i < punkteProExam.size(); i++) {
            int gesamtPunkte = punkteProExam.get(i);
            int erreichtePunkte = erreichtePunkteProExam.get(i);

            examBestanden.add(erreichtePunkte >= gesamtPunkte * 0.5);
        }

        return examBestanden.stream().allMatch(Boolean::booleanValue);
    }

    public List<Integer> berechneTestPunkte(List<Exam> exams, List<Frage> fragen) {
        List<Integer> punkteProExam = new ArrayList<>();

        for (Exam exam : exams) {
            int gesamtPunkte = fragen.stream()
                    .filter(f -> f.getExamUUID().equals(exam.getFachId()))
                    .mapToInt(Frage::getMaxPunkte)
                    .sum();
            punkteProExam.add(gesamtPunkte);
        }
        return punkteProExam;
    }

    public List<Integer> berechneErreichtePunkte(List<Exam> exams, List<Frage> fragen, List<Review> bewertungen,
                                                 List<Antwort> antworten, UUID student)  {
        List<Integer> erreichtePunkteProExam = new ArrayList<>();

        for (Exam exam : exams) {
            List<Frage> examFragen = fragen.stream()
                    .filter(f -> f.getExamUUID().equals(exam.getFachId()))
                    .toList();

            int gesamtPunkte = 0;

            for (Frage frage: examFragen) {
                Optional<Antwort> studentAntwort = antworten.stream()
                        .filter(a -> a.getStudentUUID().equals(student) && a.getFrageFachId().equals(frage.getFachId()))
                        .findFirst();

                if (studentAntwort.isPresent()) {
                    Optional<Review> review = bewertungen.stream()
                            .filter(r -> r.getAntwortFachId().equals(studentAntwort.get().getFachId()))
                            .findFirst();

                    if (review.isPresent()) {
                        gesamtPunkte += review.get().getPunkte();
                    }
                }
            }

            erreichtePunkteProExam.add(gesamtPunkte);
        }

        return erreichtePunkteProExam;
    }

}
