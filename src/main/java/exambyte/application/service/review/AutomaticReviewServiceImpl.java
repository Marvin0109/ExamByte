package exambyte.application.service.review;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ReviewDTO;
import exambyte.domain.model.user.AutoReviewer;
import exambyte.domain.service.AnswerParser;
import exambyte.domain.service.McScoringPolicy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomaticReviewServiceImpl implements AutomaticReviewService {

    private final McScoringPolicy mcScoringPolicy;
    private final AnswerParser answerParser;

    public AutomaticReviewServiceImpl(McScoringPolicy mcScoringPolicy, AnswerParser answerParser) {
        this.mcScoringPolicy = mcScoringPolicy;
        this.answerParser = answerParser;
    }

    @Override
    public List<ReviewDTO> autoReviewSC(List<QuestionDTO> questions,
                                        List<AnswerDTO> answers,
                                        List<CorrectAnswersDTO> correctAnswers,
                                        UUID studentId) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (QuestionDTO questionDTO : questions) {
            Optional<AnswerDTO> studentAnswer = findStudentAnswer(questionDTO, answers, studentId);

            if (studentAnswer.isPresent()) {
                Optional<CorrectAnswersDTO> correctAnswer = findCorrectAnswer(correctAnswers, questionDTO.id());
                if (correctAnswer.isPresent()) {
                    String solution = correctAnswer.get().solution();

                    boolean isCorrect = studentAnswer.get().answer().equals(solution);

                    reviewDTOList.add(createReview(studentAnswer.get().id(), solution,
                            isCorrect ? questionDTO.points() : 0));
                }
            }
        }
        return reviewDTOList;
    }

    @Override
    public List<ReviewDTO> autoReviewMC(List<QuestionDTO> questions,
                                                List<AnswerDTO> answers,
                                                List<CorrectAnswersDTO> correctAnswers,
                                                UUID studentId) {

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (QuestionDTO questionDTO : questions) {
            Optional<AnswerDTO> studentAnswer = findStudentAnswer(questionDTO, answers, studentId);

            if (studentAnswer.isPresent()) {
                Optional<CorrectAnswersDTO> correctAnswer = findCorrectAnswer(correctAnswers, questionDTO.id());

                if (correctAnswer.isPresent()) {
                    List<String> solutionParsed = answerParser.parseAnswer(correctAnswer.get().solution());
                    List<String> studentAnswerParsed = answerParser.parseAnswer(studentAnswer.get().answer());

                    Set<String> solutionSet = new HashSet<>(solutionParsed);

                    int correctAnswersCount = (int) studentAnswerParsed.stream().filter(solutionSet::contains).count();
                    int wrongAnswersCount = (int) studentAnswerParsed.stream()
                            .filter(a -> !solutionSet.contains(a)).count();
                    double points = mcScoringPolicy.computeMcPoints(correctAnswersCount, wrongAnswersCount,
                            solutionParsed.size(), questionDTO.points());

                    String solutionParsedText = String.join("; ", solutionParsed);
                    reviewDTOList.add(createReview(studentAnswer.get().id(), solutionParsedText, points));
                }
            }
        }
        return reviewDTOList;
    }

    private static Optional<AnswerDTO> findStudentAnswer(QuestionDTO question,
                                                         List<AnswerDTO> answers,
                                                         UUID studentId) {
        return answers.stream()
                .filter(a -> a.studentId().equals(studentId)
                        && a.questionId().equals(question.id()))
                .findFirst();
    }

    private static Optional<CorrectAnswersDTO> findCorrectAnswer(List<CorrectAnswersDTO> correctAnswers,
                                                                 UUID questionId) {
        return correctAnswers.stream()
                .filter(q -> q.questionId().equals(questionId))
                .findFirst();
    }

    private static ReviewDTO createReview(UUID answerId, String solution, double points) {
        return new ReviewDTO(null, answerId, AutoReviewer.AUTOMATIC_REVIEWER, "Lösung: " + solution, points);
    }
}
