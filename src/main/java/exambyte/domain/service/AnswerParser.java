package exambyte.domain.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AnswerParser {

    public List<String> parseAnswer(String answer) {
        return Arrays.stream(answer.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
