package exambyte.application.service.review;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomaticReviewServiceImpl implements AutomaticReviewService {

    private static final UUID AUTO_REVIEW_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Override
    public List<ReviewDTO> automatischeReviewSC(List<FrageDTO> fragen,
                                                List<AntwortDTO> antworten,
                                                List<KorrekteAntwortenDTO> korrekteAntworten,
                                                UUID studentUUID,
                                                ReviewService reviewService) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (FrageDTO frageDTO : fragen) {
            Optional<AntwortDTO> studentAntwort = antworten.stream()
                    .filter(a -> a.studentId().equals(studentUUID) &&
                            a.frageId().equals(frageDTO.id()))
                    .findFirst();

            if (studentAntwort.isPresent()) {
                Optional<KorrekteAntwortenDTO> korrekteAntwort = korrekteAntworten.stream()
                        .filter(k -> k.frageId().equals(frageDTO.id()))
                        .findFirst();

                if (korrekteAntwort.isPresent()) {
                    String richtigeAntwort = korrekteAntwort.get().antworten();
                    boolean isCorrect = studentAntwort.get().antwortText().equals(richtigeAntwort);

                    ReviewDTO review = new ReviewDTO(null, studentAntwort.get().id(),
                            AUTO_REVIEW_ID, "Lösung: " + richtigeAntwort,
                            isCorrect ? frageDTO.maxPunkte() : 0);
                    
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
            Optional<AntwortDTO> studentAntwort = findStudentAntwort(frageDTO, antworten, studentUUID);

            if (studentAntwort.isPresent()) {
                Optional<KorrekteAntwortenDTO> korrekteAntwort = answers.stream()
                        .filter(k -> k.frageId().equals(frageDTO.id()))
                        .findFirst();

                if (korrekteAntwort.isPresent()) {
                    List<String> richtigeAntworten = parseAntworten(korrekteAntwort.get().antworten());
                    List<String> studentAntworten = parseAntworten(studentAntwort.get().antwortText());

                    Set<String> richtigeSet = new HashSet<>(richtigeAntworten);

                    int correctAnswers = (int) studentAntworten.stream().filter(richtigeSet::contains).count();
                    int wrongAnswers = (int) studentAntworten.stream()
                            .filter(a -> !richtigeSet.contains(a)).count();

                    double points = computeMcPoints(correctAnswers, wrongAnswers,
                            richtigeAntworten.size(), frageDTO.maxPunkte());

                    String richtigeAntwortenText = String.join("; ", richtigeAntworten);

                    ReviewDTO review = new ReviewDTO(null, studentAntwort.get().id(),
                            AUTO_REVIEW_ID, "Lösung: " + richtigeAntwortenText, points);
                    reviewDTOList.add(review);
                }
            }
        }
        return reviewDTOList;
    }

    private static Optional<AntwortDTO> findStudentAntwort(FrageDTO frage, List<AntwortDTO> antworten, UUID studentId) {
        return antworten.stream()
                .filter(a -> a.studentId().equals(studentId)
                        && a.frageId().equals(frage.id()))
                .findFirst();
    }

    private static List<String> parseAntworten(String antwortText) {
        return Arrays.stream(antwortText.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private static double computeMcPoints(int correctAnswers,
                                          int wrongAnswers,
                                          int totalCorrectAnswers,
                                          double maxPunkte) {
        if (totalCorrectAnswers <= 0) return 0.0;

        double pointsPerCorrect = maxPunkte / totalCorrectAnswers;
        double points = (correctAnswers - wrongAnswers) * pointsPerCorrect;
        points = Math.max(0.0, points);

        // Auf 0.5 Punkte runden
        return Math.round(points * 2) / 2.0;
    }
}
