package exambyte.application.service;

import exambyte.application.dto.AntwortDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.mapper.ReviewDTOMapper;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomaticReviewServiceImpl implements AutomaticReviewService {

    private final ReviewService reviewService;
    private final ReviewDTOMapper reviewDTOMapper;

    public AutomaticReviewServiceImpl(ReviewService reviewService, ReviewDTOMapper reviewDTOMapper) {
        this.reviewService = reviewService;
        this.reviewDTOMapper = reviewDTOMapper;
    }

    @Override
    public List<ReviewDTO> automatischeReviewSC(List<FrageDTO> fragen, List<AntwortDTO> antworten,
                                                List<KorrekteAntwortenDTO> korrekteAntworten, UUID studentUUID) {

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

                    ReviewDTO review = new ReviewDTO(null, null, studentAntwort.get().getFachId(),
                            null, "Lösung: " + richtigeAntwort, isCorrect ? 1 : 0);
                    
                    reviewDTOList.add(review);
                    reviewService.addReview(reviewDTOMapper.toDomain(review));
                }
            }
        }
        return reviewDTOList;
    }

    @Override
    public List<ReviewDTO> automatischeReviewMC(List<FrageDTO> fragen, List<AntwortDTO> antworten, List<KorrekteAntwortenDTO> answers,
                                                UUID studentUUID) {

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
                    List<String> richtigeAntworten = Arrays.asList(korrekteAntwort.get().getAntworten().split(","));
                    List<String> studentAntworten = Arrays.asList(studentAntwort.get().getAntwortText().split(","));

                    // Entferne Leerzeichen
                    richtigeAntworten = richtigeAntworten.stream().map(String::trim).toList();
                    studentAntworten = studentAntworten.stream().map(String::trim).toList();

                    int points = getPoints(studentAntworten, richtigeAntworten);

                    String richtigeAntwortenText = String.join(", ", richtigeAntworten);

                    ReviewDTO review = new ReviewDTO(null, null, studentAntwort.get().getFachId(),
                            null, "Lösung: " + richtigeAntwortenText, points);
                    reviewDTOList.add(review);
                    reviewService.addReview(reviewDTOMapper.toDomain(review));
                }
            }
        }
        return reviewDTOList;
    }

    public static int getPoints(List<String> studentAntworten, List<String> richtigeAntworten) {
        int correctAnswers = 0;
        int wrongAnswers = 0;

        for (String antwort : studentAntworten) {
            if (richtigeAntworten.contains(antwort)) {
                correctAnswers++;
            } else {
                wrongAnswers++;
            }
        }

        int totalAnswers = richtigeAntworten.size();
        int maxWrongAnswers = totalAnswers / 2;

        int points = 0;

        if (wrongAnswers <= maxWrongAnswers) {
            points = correctAnswers - wrongAnswers;
        }

        points = Math.max(0, points);
        return points;
    }
}
