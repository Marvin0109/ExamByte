package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomaticReviewServiceImpl implements AutomaticReviewService {

    private static final UUID AUTO_REVIEW_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Override
    public List<ReviewDTO> autoReviewSC(List<QuestionDTO> questions,
                                                List<AnswerDTO> answers,
                                                List<CorrectAnswersDTO> correctAnswers,
                                                UUID studentId,
                                                ReviewService reviewService) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (QuestionDTO questionDTO : questions) {
            Optional<AnswerDTO> studentAnswer = answers.stream()
                    .filter(a -> a.studentId().equals(studentId) &&
                            a.questionId().equals(questionDTO.id()))
                    .findFirst();

            if (studentAnswer.isPresent()) {
                Optional<CorrectAnswersDTO> correctAnswer = correctAnswers.stream()
                        .filter(k -> k.questionId().equals(questionDTO.id()))
                        .findFirst();

                if (correctAnswer.isPresent()) {
                    String solution = correctAnswer.get().solution();
                    boolean isCorrect = studentAnswer.get().answer().equals(solution);

                    ReviewDTO review = new ReviewDTO(null, studentAnswer.get().id(),
                            AUTO_REVIEW_ID, "Lösung: " + solution,
                            isCorrect ? questionDTO.points() : 0);
                    
                    reviewDTOList.add(review);
                }
            }
        }
        return reviewDTOList;
    }

    @Override
    public List<ReviewDTO> autoReviewMC(List<QuestionDTO> questions,
                                                List<AnswerDTO> answers,
                                                List<CorrectAnswersDTO> correctAnswers,
                                                UUID studentId,
                                                ReviewService reviewService) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (QuestionDTO questionDTO : questions) {
            Optional<AnswerDTO> studentAnswer = findStudentAnswer(questionDTO, answers, studentId);

            if (studentAnswer.isPresent()) {
                Optional<CorrectAnswersDTO> correctAnswer = correctAnswers.stream()
                        .filter(k -> k.questionId().equals(questionDTO.id()))
                        .findFirst();

                if (correctAnswer.isPresent()) {
                    List<String> solutionParsed = parseAnswer(correctAnswer.get().solution());
                    List<String> studentAnswerParsed = parseAnswer(studentAnswer.get().answer());

                    Set<String> richtigeSet = new HashSet<>(solutionParsed);

                    int correctAnswersCount = (int) studentAnswerParsed.stream().filter(richtigeSet::contains).count();
                    int wrongAnswersCount = (int) studentAnswerParsed.stream()
                            .filter(a -> !richtigeSet.contains(a)).count();

                    double points = computeMcPoints(correctAnswersCount, wrongAnswersCount,
                            solutionParsed.size(), questionDTO.points());

                    String solutionParsedText = String.join("; ", solutionParsed);

                    ReviewDTO review = new ReviewDTO(null, studentAnswer.get().id(),
                            AUTO_REVIEW_ID, "Lösung: " + solutionParsedText, points);
                    reviewDTOList.add(review);
                }
            }
        }
        return reviewDTOList;
    }

    private static Optional<AnswerDTO> findStudentAnswer(QuestionDTO frage, List<AnswerDTO> answers, UUID studentId) {
        return answers.stream()
                .filter(a -> a.studentId().equals(studentId)
                        && a.questionId().equals(frage.id()))
                .findFirst();
    }

    private static List<String> parseAnswer(String answer) {
        return Arrays.stream(answer.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private static double computeMcPoints(int correctAnswers,
                                          int wrongAnswers,
                                          int totalCorrectAnswers,
                                          double totalPoints) {
        if (totalCorrectAnswers <= 0) return 0.0;

        double pointsPerCorrect = totalPoints / totalCorrectAnswers;
        double points = (correctAnswers - wrongAnswers) * pointsPerCorrect;
        points = Math.max(0.0, points);

        // round to half steps
        return Math.round(points * 2) / 2.0;
    }
}
