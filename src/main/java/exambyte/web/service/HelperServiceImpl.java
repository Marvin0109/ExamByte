package exambyte.web.service;

import exambyte.application.enums.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamFacadeService;
import exambyte.domain.model.user.AutoReviewer;
import exambyte.web.form.load_old_submit_data.OldDataDTO;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_exam.ExamAggregateDTO;
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewAggregateDTO;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HelperServiceImpl implements HelperService {

    private final ExamFacadeService service;
    private final Clock clock;

    public HelperServiceImpl(ExamFacadeService service, Clock clock) {
        this.service = service;
        this.clock = clock;
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public List<AttemptDTO> getValidAttempts(String studentName) {
        List<ExamDTO> exams = service.getAllExams();
        List<AttemptDTO> allValidAttempts = new ArrayList<>();

        for (ExamDTO exam : exams) {
            AttemptDTO attempt = service.getSubmission(exam.id(), studentName);
            if (exam.result().isBefore(now())) {
                allValidAttempts.add(attempt);
            }
        }

        return allValidAttempts;
    }

    @Override
    public String getExamAvailabilityNotice(ExamDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy, HH:mm");

        if (now().isBefore(dto.start())) {
            String startTimeFormatted = dto.start().format(formatter);

            return "Der Test kann erst ab den " + startTimeFormatted + " bearbeitet werden.";
        }

        if (now().isAfter(dto.end()) || now().isEqual(dto.end())) {
            String endTimeFormatted = dto.end().format(formatter);

            return "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten. Der Test " +
                    "konnte nur bis " + endTimeFormatted + " bearbeitet werden.";
        }

        return "";
    }

    @Override
    public String getTimeDifference(ExamDTO examDTO) {
        StringBuilder deadlineDisplay = new StringBuilder();
        String daysDisplay = "";
        String hoursDisplay = "";
        String minutesDisplay = "";

        Duration diff = Duration.between(now(),
                examDTO.end().truncatedTo(ChronoUnit.MINUTES));

        long days = diff.toDays();
        long hours = diff.toHours() % 24;
        long minutes = diff.toMinutes() % 60;

        if (days == 1) {
            daysDisplay = days + " Tag";
        } else if (days > 1) {
            daysDisplay = days + " Tage";
        }

        if (hours == 1) {
            hoursDisplay = hours + " Stunde";
        } else if (hours > 1) {
            hoursDisplay = hours + " Stunden";
        }

        if (minutes == 1) {
            minutesDisplay = minutes + " Minute";
        } else if (minutes > 1) {
            minutesDisplay = minutes + " Minuten";
        }

        if (!daysDisplay.isEmpty()) deadlineDisplay.append(daysDisplay).append(" ");
        if (!hoursDisplay.isEmpty()) deadlineDisplay.append(hoursDisplay).append(" ");
        if (!minutesDisplay.isEmpty()) deadlineDisplay.append(minutesDisplay).append(" ");
        return deadlineDisplay.toString();
    }

    @Override
    public String normalizeAnswerForFrontend(String toSplit) {
        List<String> lines = Arrays.stream(toSplit.split("\n"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(s -> s.replace(",", "ĸ"))
                        .toList();

        return String.join(",", lines);
    }

    @Override
    public PreparedQuestionData prepareFrageData(QuestionDTO question, UUID studentId) {
        AnswerDTO answer = service.getAnswerForQuestionIdAndStudentId(question.id(), studentId);
        CorrectAnswersDTO correctAnswers = service.getCorrectAnswerForQuestion(question.id());

        if (question.type().name().equals("MC") || question.type().name().equals("SC")) {
            correctAnswers = normalizeCorrectAnswers(correctAnswers);

            if (answer != null) {
                String normalized = normalizeAnswerForFrontend(answer.answer());
                answer = new AnswerDTO(
                        answer.id(),
                        normalized,
                        answer.questionId(),
                        answer.studentId(),
                        answer.submitTime()
                );
            }
        }

        return new PreparedQuestionData(question, answer, correctAnswers);
    }

    private CorrectAnswersDTO normalizeCorrectAnswers(CorrectAnswersDTO correctAnswers) {
        String choicesNormalized = normalizeAnswerForFrontend(correctAnswers.choices());
        String solutionNormalized = normalizeAnswerForFrontend(correctAnswers.solution());

        return new CorrectAnswersDTO(
                correctAnswers.id(),
                solutionNormalized,
                choicesNormalized,
                correctAnswers.questionId()
        );
    }

    @Override
    public ReviewViewForm prepareReviewViewForm(UUID examId, String studentName) {
        ExamDTO exam = service.getExam(examId);
        UUID studentId = service.getStudentIdByName(studentName);
        AttemptDTO attempt = service.getSubmission(examId, studentName);

        List<QuestionDTO> questions = service.getQuestionsForExam(examId);
        List<ReviewAggregateDTO> componentList = new ArrayList<>();
        List<UUID> reviewers = new ArrayList<>();

        for (QuestionDTO question : questions) {
            PreparedQuestionData preparedQuestionData = prepareFrageData(question, studentId);
            AnswerDTO answer = preparedQuestionData.answer();

            CorrectAnswersDTO k = preparedQuestionData.correctAnswers();

            if (answer != null) {
                ReviewDTO review = service.getReviewForAnswer(answer.id());
                if (review != null) reviewers.add(review.reviewerId());
                componentList.add(new ReviewAggregateDTO(question, answer, review, k));
            } else {
                AnswerDTO emptyAnswer = new AnswerDTO(
                        null,
                        "",
                        question.id(),
                        studentId,
                        null
                );

                ReviewDTO emptyReview = new ReviewDTO(
                        null,
                        null,
                        null,
                        "Keine Bewertung",
                        0
                );

                componentList.add(new ReviewAggregateDTO(question, emptyAnswer, emptyReview, k));
            }
        }

        String reviewerNames = reviewers.stream()
                .map(service::getReviewerById)
                .map(ReviewerDTO::name)
                .filter(name -> !name.equals(AutoReviewer.getName()))
                .distinct()
                .collect(Collectors.joining(", "));

        return new ReviewViewForm(
                exam.title(),
                reviewerNames,
                attempt.accumulatedPoints(),
                attempt.totalPoints(),
                componentList);
    }

    @Override
    public OldDataForm fillOldDataForm(UUID examId, String studentName) {
        ExamDTO exam = service.getExam(examId);

        UUID studentId = service.getStudentIdByName(studentName);

        List<QuestionDTO> questions = service.getQuestionsForExam(examId);

        List<OldDataDTO> oldDataDTOList = new ArrayList<>();

        for (QuestionDTO frage : questions) {
            PreparedQuestionData preparedQuestionData = prepareFrageData(frage, studentId);

            OldDataDTO oldDataDTO = new OldDataDTO(
                    frage,
                    preparedQuestionData.correctAnswers(),
                    preparedQuestionData.answer());

            oldDataDTOList.add(oldDataDTO);
        }

        return new OldDataForm(examId, exam.title(), oldDataDTOList);
    }

    @Override
    public SubmitForm fillSubmitFormWithData(OldDataForm form) {
        SubmitForm submitForm = new SubmitForm();
        Map<String, List<String>> answers = new HashMap<>();
        List<OldDataDTO> oldDataDTOList = form.oldDataDTOs();

        for (OldDataDTO oldDataDTO : oldDataDTOList) {
            String questionId = String.valueOf(oldDataDTO.question().id());
            boolean answerIsPresent = oldDataDTO.answer() != null && oldDataDTO.answer().answer() != null;

            if (Objects.requireNonNull(oldDataDTO.question().type()) == QuestionTypeDTO.MC) {
                if (answerIsPresent) {
                    String answer = oldDataDTO.answer().answer();
                    List<String> choices = Arrays.stream(answer.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();

                    answers.put(questionId, choices);
                } else {
                    answers.put(questionId, new ArrayList<>());
                }
            } else {
                if (answerIsPresent) {
                    String normalized = oldDataDTO.answer().answer();
                    answers.put(questionId, List.of(normalized));
                } else {
                    answers.put(questionId, Collections.singletonList(""));
                }
            }
        }

        submitForm.setAnswers(answers);
        return submitForm;
    }

    @Override
    public ExamViewForm prepareExamViewForm(UUID examId) {
        ExamDTO exam = service.getExam(examId);
        ProfessorDTO prof = service.getProfessor(exam.professorId());
        List<QuestionDTO> questions = service.getQuestionsForExam(examId);

        List<ExamAggregateDTO> components = new ArrayList<>();

        double totalPoints = 0;

        for (QuestionDTO question : questions) {
            totalPoints += question.points();
            CorrectAnswersDTO correctAnswers = service.getCorrectAnswerForQuestion(question.id());

            if (correctAnswers != null) {
                correctAnswers = normalizeCorrectAnswers(correctAnswers);
                components.add(new ExamAggregateDTO(question, correctAnswers));
            } else {
                components.add(new ExamAggregateDTO(question, null));
            }
        }

        return new ExamViewForm(
                exam.title(),
                prof.name(),
                totalPoints,
                components);
    }
}
