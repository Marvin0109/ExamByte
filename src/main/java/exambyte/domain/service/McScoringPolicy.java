package exambyte.domain.service;

import org.springframework.stereotype.Service;

@Service
public class McScoringPolicy {

    public double computeMcPoints(int correctAnswers,
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
