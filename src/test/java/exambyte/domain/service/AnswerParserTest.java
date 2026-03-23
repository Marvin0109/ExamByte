package exambyte.domain.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerParserTest {

    private final AnswerParser answerParser = new AnswerParser();

    @Test
    void parseAnswer_01() {
        String answer = "A\nB\nC";

        List<String> result = answerParser.parseAnswer(answer);

        assertThat(result).containsExactly("A", "B", "C");
    }

    @Test
    void parseAnswer_02() {
        String answer = "A, B\nC";

        List<String> result = answerParser.parseAnswer(answer);

        assertThat(result).containsExactly("A, B", "C");
    }
}
