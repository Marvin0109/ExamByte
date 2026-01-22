package exambyte.application.service;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomaticReviewServiceImpl implements AutomaticReviewService {

    @Override
    public List<ReviewDTO> automatischeReviewSC(List<FrageDTO> fragen,
                                                List<AntwortDTO> antworten,
                                                List<KorrekteAntwortenDTO> korrekteAntworten,
                                                UUID studentUUID,
                                                ReviewService reviewService) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (FrageDTO frageDTO : fragen) {
            Optional<AntwortDTO> studentAntwort = antworten.stream()
                    .filter(a -> a.getStudentFachId().equals(studentUUID) &&
                            a.getFrageFachId().equals(frageDTO.getFachId()))
                    .findFirst();

            if (studentAntwort.isPresent()) {
                Optional<KorrekteAntwortenDTO> korrekteAntwort = korrekteAntworten.stream()
                        .filter(k -> k.getFrageFachID().equals(frageDTO.getFachId()))
                        .findFirst();

                if (korrekteAntwort.isPresent()) {
                    String richtigeAntwort = korrekteAntwort.get().getAntworten();
                    boolean isCorrect = studentAntwort.get().getAntwortText().equals(richtigeAntwort);

                    // UUID für automatische Review
                    UUID automaticKorrektor = UUID.fromString("11111111-1111-1111-1111-111111111111");

                    ReviewDTO review = new ReviewDTO(null, UUID.randomUUID(), studentAntwort.get().getFachId(),
                            automaticKorrektor, "Lösung: " + richtigeAntwort, isCorrect ? frageDTO.getMaxPunkte() : 0);
                    
                    reviewDTOList.add(review);
                }
            }
        }
        return reviewDTOList;
    }

    @Override
    public List<ReviewDTO> automatischeReviewMC(List<FrageDTO> fragen,
                                                List<AntwortDTO> antworten,
                                                List<KorrekteAntwortenDTO> answers,
                                                UUID studentUUID,
                                                ReviewService reviewService) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (FrageDTO frageDTO : fragen) {
            Optional<AntwortDTO> studentAntwort = antworten.stream()
                    .filter(a -> a.getStudentFachId().equals(studentUUID) && a.getFrageFachId().equals(frageDTO.getFachId()))
                    .findFirst();

            if (studentAntwort.isPresent()) {
                Optional<KorrekteAntwortenDTO> korrekteAntwort = answers.stream()
                        .filter(k -> k.getFrageFachID().equals(frageDTO.getFachId()))
                        .findFirst();

                if (korrekteAntwort.isPresent()) {
                    List<String> richtigeAntworten = Arrays.stream(korrekteAntwort.get().getAntworten().split("\\r?\\n|,"))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();

                    List<String> studentAntworten = Arrays.stream(studentAntwort.get().getAntwortText().split("\\r?\\n|,"))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();

                    Set<String> richtigeSet = new HashSet<>(richtigeAntworten);

                    int correctAnswers = (int) studentAntworten.stream().filter(richtigeSet::contains).count();
                    int wrongAnswers = (int) studentAntworten.stream().filter(a -> !richtigeSet.contains(a)).count();

                    int points = computeMcPoints(correctAnswers, wrongAnswers, richtigeAntworten.size(), frageDTO.getMaxPunkte());

                    String richtigeAntwortenText = String.join(", ", richtigeAntworten);

                    // UUID für automatische Review
                    UUID automaticKorrektor = UUID.fromString("11111111-1111-1111-1111-111111111111");

                    ReviewDTO review = new ReviewDTO(null, UUID.randomUUID(), studentAntwort.get().getFachId(),
                            automaticKorrektor, "Lösung: " + richtigeAntwortenText, points);
                    reviewDTOList.add(review);
                }
            }
        }
        return reviewDTOList;
    }

    private static int computeMcPoints(int correctAnswers, int wrongAnswers, int totalCorrectAnswers, int maxPunkte) {
        if (totalCorrectAnswers <= 0) return 0;

        int maxWrongAllowed = totalCorrectAnswers / 2;
        if (wrongAnswers > maxWrongAllowed) return 0;

        // fraction = (correct - wrong) / totalCorrectAnswers
        double fraction = (double)(correctAnswers - wrongAnswers) / (double) totalCorrectAnswers;
        fraction = Math.max(0.0, fraction); // nie negativ

        // Punkte als gerundeter Anteil von maxPunkte
        return (int) Math.round(fraction * maxPunkte);
    }
}
