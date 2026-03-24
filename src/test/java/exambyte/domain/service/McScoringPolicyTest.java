package exambyte.domain.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class McScoringPolicyTest {

    private final McScoringPolicy mcScoringPolicy = new McScoringPolicy();

    @ParameterizedTest(name = "{0}")
    @MethodSource("mcTestCases")
    void computeMcPoints(
            String description,
            int correctAnswers,
            int wrongAnswers,
            int totalCorrectAnswers,
            double totalPoints,
            double expected
    ) {
        double result = mcScoringPolicy.computeMcPoints(correctAnswers, wrongAnswers, totalCorrectAnswers, totalPoints);

        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> mcTestCases() {
        return Stream.of(
                Arguments.of("Everything correct", 4, 0, 4, 4.0, 4.0),
                Arguments.of("3 correct, 0 wrong", 3, 0, 4, 4.0, 3.0),
                Arguments.of("3 correct, 1 wrong", 3, 1, 4, 4.0, 2.0),
                Arguments.of("2 correct, 0 wrong", 2, 0, 4, 4.0, 2.0),
                Arguments.of("2 correct, 1 wrong", 2, 1, 4, 4.0, 1.0),
                Arguments.of("2 correct, 2 wrong", 2, 2, 4, 4.0, 0.0),
                Arguments.of("1 correct, 0 wrong", 1, 0, 4, 4.0, 1.0),
                Arguments.of("1 correct, 1 wrong", 1, 1, 4, 4.0, 0.0),
                Arguments.of("0 correct, 0 wrong", 0, 0, 4, 4.0, 0.0),

                Arguments.of("Everything correct", 4, 0, 4, 3.5, 3.5),
                Arguments.of("3 correct, 0 wrong", 3, 0, 4, 3.5, 2.5),
                Arguments.of("3 correct, 1 wrong", 3, 1, 4, 3.5, 2.0),
                Arguments.of("2 correct, 1 wrong", 2, 1, 4, 3.5, 1.0),
                Arguments.of("1 correct, 1 wrong", 1, 1, 4, 3.5, 0.0),
                Arguments.of("Everything wrong", 0, 4, 4, 3.5, 0.0),

                Arguments.of("3 correct, 1 wrong", 3, 1, 4, 2.0, 1.0),
                Arguments.of("2 correct, 1 wrong", 2, 1, 4, 2.0, 0.5)
        );
    }
}
