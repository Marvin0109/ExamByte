package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamFacadeService;
import exambyte.web.form.load_old_submit_data.OldDataDTO;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewAggregateDTO;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HelperServiceImpl implements HelperService {

    private final ExamFacadeService service;

    public HelperServiceImpl(ExamFacadeService service) {
        this.service = service;
    }

    @Override
    public List<VersuchDTO> getValidAttempts(String studentName) {
        List<ExamDTO> exams = service.getAllExams();
        List<VersuchDTO> allValidAttempts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        for (ExamDTO exam : exams) {
            VersuchDTO v = service.getSubmission(exam.fachId(), studentName);
            if (exam.resultTime().isBefore(now)) {
                allValidAttempts.add(v);
            }
        }

        return allValidAttempts;
    }

    @Override
    public String getExamAvailabilityNotice(ExamDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy, HH:mm");
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(dto.startTime())) {
            String startTimeFormatted = dto.startTime().format(formatter);

            return "Der Test kann erst ab den " + startTimeFormatted + " bearbeitet werden.";
        }

        if (now.isAfter(dto.endTime())) {
            String endTimeFormatted = dto.endTime().format(formatter);

            return "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten. Der Test " +
                    "konnte nur bis " + endTimeFormatted + " bearbeitet werden.";
        }

        return "";
    }

    @Override
    public String getTimeDifference(ExamDTO examDTO) {
        StringBuilder fristAnzeige = new StringBuilder();
        String tageAnzeige = "";
        String stundenAnzeige = "";
        String minutenAnzeige = "";

        Duration diff = Duration.between(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                examDTO.endTime().truncatedTo(ChronoUnit.MINUTES));

        long days = diff.toDays();
        long hours = diff.toHours() % 24;
        long minutes = diff.toMinutes() % 60;

        if (days == 1) {
            tageAnzeige = days + " Tag";
        } else if (days > 1) {
            tageAnzeige = days + " Tage";
        }

        if (hours == 1) {
            stundenAnzeige = hours + " Stunde";
        } else if (hours > 1) {
            stundenAnzeige = hours + " Stunden";
        }

        if (minutes == 1) {
            minutenAnzeige = minutes + " Minute";
        } else if (minutes > 1) {
            minutenAnzeige = minutes + " Minuten";
        }

        if (!tageAnzeige.isEmpty()) fristAnzeige.append(tageAnzeige).append(" ");
        if (!stundenAnzeige.isEmpty()) fristAnzeige.append(stundenAnzeige).append(" ");
        if (!minutenAnzeige.isEmpty()) fristAnzeige.append(minutenAnzeige).append(" ");
        return fristAnzeige.toString();
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
    public PreparedFrageData prepareFrageData(FrageDTO frage, UUID studentId) {
        AntwortDTO antwort = service.getAntwortForFrageAndStudent(frage.fachId(), studentId);
        KorrekteAntwortenDTO k = service.getLoesungForFrage(frage.fachId());

        if (antwort != null && (frage.type().name().equals("MC") || frage.type().name().equals("SC"))) {
            String normalized = normalizeAnswerForFrontend(antwort.antwortText());
            antwort = new AntwortDTO(
                    antwort.fachId(),
                    normalized,
                    antwort.frageFachId(),
                    antwort.studentFachId(),
                    antwort.antwortZeitpunkt()
            );

            String optionen = k.antwortOptionen();
            String optionenNormalized = normalizeAnswerForFrontend(optionen);

            String loesung = k.antworten();
            String loesungNormalized = normalizeAnswerForFrontend(loesung);

            k = new KorrekteAntwortenDTO(
                    k.fachId(),
                    loesungNormalized,
                    optionenNormalized,
                    k.frageFachId()
            );
        }

        return new PreparedFrageData(frage, antwort, k);
    }

    @Override
    public ReviewViewForm prepareReviewViewForm(UUID examId, String studentName) {
        ExamDTO exam = service.getExam(examId);
        UUID studentId = service.getStudentIdByName(studentName);
        VersuchDTO versuch = service.getSubmission(examId, studentName);

        List<FrageDTO> fragen = service.getFragenForExam(examId);
        List<ReviewAggregateDTO> componentList = new ArrayList<>();
        List<UUID> korrektoren = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            PreparedFrageData preparedFrageData = prepareFrageData(frage, studentId);
            AntwortDTO antwort = preparedFrageData.antwort();

            KorrekteAntwortenDTO k = preparedFrageData.korrekteAntwortenDTO();

            ReviewDTO review = null;
            if (antwort != null) {
                review = service.getReviewForAntwort(antwort.fachId());
                if (review != null) korrektoren.add(review.korrektorFachId());
            }

            componentList.add(new ReviewAggregateDTO(frage, antwort, review, k));
        }

        String korrektorNames = korrektoren.stream()
                .map(service::getReviewerById)
                .map(KorrektorDTO::name)
                .filter(name -> !name.equals("Automatischer Korrektor"))
                .distinct()
                .collect(Collectors.joining(", "));

        return new ReviewViewForm(
                exam.title(),
                korrektorNames,
                versuch.erreichtePunkte(),
                versuch.maxPunkte(),
                componentList);
    }

    @Override
    public OldDataForm fillOldDataForm(UUID examId, String studentName) {
        ExamDTO exam = service.getExam(examId);

        UUID studentId = service.getStudentIdByName(studentName);

        List<FrageDTO> fragen = service.getFragenForExam(examId);

        List<OldDataDTO> oldDataDTOList = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            PreparedFrageData preparedFrageData = prepareFrageData(frage, studentId);

            OldDataDTO oldDataDTO = new OldDataDTO(
                    frage,
                    preparedFrageData.korrekteAntwortenDTO(),
                    preparedFrageData.antwort());

            oldDataDTOList.add(oldDataDTO);
        }

        return new OldDataForm(examId, exam.title(), oldDataDTOList);
    }

    @Override
    public SubmitForm fillSubmitFormWithData(OldDataForm form) {
        SubmitForm submitForm = new SubmitForm();

        Map<String, List<String>> answers = new HashMap<>();

        List<OldDataDTO> oldDataDTOList = form.components();

        for (OldDataDTO oldDataDTO : oldDataDTOList) {

            String frageId = String.valueOf(oldDataDTO.fragen().fachId());
            boolean answerIsPresent = oldDataDTO.antwort() != null && oldDataDTO.antwort().antwortText() != null;

            if (Objects.requireNonNull(oldDataDTO.fragen().type()) == QuestionTypeDTO.MC) {
                if (answerIsPresent) {
                    String answer = oldDataDTO.antwort().antwortText();
                    List<String> choices = Arrays.stream(answer.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();

                    answers.put(frageId, choices);
                } else {
                    answers.put(frageId, new ArrayList<>());
                }
            } else {
                if (answerIsPresent) {
                    String normalized = oldDataDTO.antwort().antwortText();
                    answers.put(frageId, List.of(normalized));
                } else {
                    answers.put(frageId, Collections.singletonList(""));
                }
            }
        }

        submitForm.setAnswers(answers);
        return submitForm;
    }
}
