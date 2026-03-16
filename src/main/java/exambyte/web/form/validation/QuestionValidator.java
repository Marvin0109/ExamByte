package exambyte.web.form.validation;

import exambyte.web.form.create_exam.QuestionData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestionValidator implements ConstraintValidator<ValidQuestion, QuestionData> {

    @Override
    public boolean isValid(QuestionData question, ConstraintValidatorContext context) {
        if (question == null) return true;

        context.disableDefaultConstraintViolation();

        String type = question.getType();
        boolean valid = true;

        switch (type) {
            case "SC":
                valid = checkSC(question, context);
                break;
            case "MC":
                valid = checkMC(question, context);
                break;
            default:
        }

        return valid;
    }

    private boolean checkSC(QuestionData question, ConstraintValidatorContext context) {
        if (checkChoices(question.getChoices(), context)) {
            return (checkCorrectAnswer(question.getCorrectAnswer(), question.getChoices(), context));
        }
        return false;
    }

    private boolean checkMC(QuestionData question, ConstraintValidatorContext context) {
        if (checkChoices(question.getChoices(), context)) {
            return (checkCorrectAnswers(question.getCorrectAnswers(), question.getChoices(), context));
        }
        return false;
    }

    private boolean checkChoices(String choices, ConstraintValidatorContext context) {
        if (isEmpty(choices)) {
            context.buildConstraintViolationWithTemplate("Antwortmöglichkeiten müssen angegeben werden")
                    .addPropertyNode("choices")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean checkCorrectAnswer(String answer, String choices, ConstraintValidatorContext context) {
        if (isEmpty(answer)) {
            context.buildConstraintViolationWithTemplate("Richtige Antwort muss angegeben werden")
                    .addPropertyNode("correctAnswer")
                    .addConstraintViolation();
            return false;
        } else if (!isEmpty(choices)) {
            Set<String> choiceSet = toSet(choices);
            if (!choiceSet.contains(answer.trim())) {
                context.buildConstraintViolationWithTemplate(
                                "Richtige Antwort muss in den Antwortmöglichkeiten enthalten sein")
                        .addPropertyNode("correctAnswer")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }

    private boolean checkCorrectAnswers(String answers, String choices, ConstraintValidatorContext context) {
        if (isEmpty(answers)) {
            context.buildConstraintViolationWithTemplate("Richtige Antworten müssen angegeben werden")
                    .addPropertyNode("correctAnswers")
                    .addConstraintViolation();
            return false;
        } else if (!isEmpty(choices)) {
            Set<String> choiceSet = toSet(choices);
            Set<String> correctSet = toSet(answers);

            for (String ans : correctSet) {
                if (!choiceSet.contains(ans)) {
                    context.buildConstraintViolationWithTemplate(
                                    "Alle richtigen Antworten müssen in den Antwortmöglichkeiten enthalten sein")
                            .addPropertyNode("correctAnswers")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }

    private Set<String> toSet(String s) {
        return Arrays.stream(s.split("\n"))
                .map(String::trim)
                .filter(o -> !o.isEmpty())
                .collect(Collectors.toSet());
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
