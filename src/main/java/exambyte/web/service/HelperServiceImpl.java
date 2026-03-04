package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamFacadeService;
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
    public List<VersuchDTO> getValidAttempts(String studentName) {
        List<ExamDTO> exams = service.getAllExams();
        List<VersuchDTO> allValidAttempts = new ArrayList<>();

        for (ExamDTO exam : exams) {
            VersuchDTO v = service.getSubmission(exam.id(), studentName);
            if (exam.resultTime().isBefore(now())) {
                allValidAttempts.add(v);
            }
        }

        return allValidAttempts;
    }

    @Override
    public String getExamAvailabilityNotice(ExamDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy, HH:mm");

        if (now().isBefore(dto.startTime())) {
            String startTimeFormatted = dto.startTime().format(formatter);

            return "Der Test kann erst ab den " + startTimeFormatted + " bearbeitet werden.";
        }

        if (now().isAfter(dto.endTime()) || now().isEqual(dto.endTime())) {
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

        Duration diff = Duration.between(now(),
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
        AntwortDTO antwort = service.getAntwortForFrageAndStudent(frage.id(), studentId);
        KorrekteAntwortenDTO k = service.getLoesungForFrage(frage.id());

        if (frage.type().name().equals("MC") || frage.type().name().equals("SC")) {
            String optionen = k.antwortOptionen();
            String optionenNormalized = normalizeAnswerForFrontend(optionen);

            String loesung = k.antworten();
            String loesungNormalized = normalizeAnswerForFrontend(loesung);

            k = new KorrekteAntwortenDTO(
                    k.id(),
                    loesungNormalized,
                    optionenNormalized,
                    k.frageId()
            );

            if (antwort != null) {
                String normalized = normalizeAnswerForFrontend(antwort.antwortText());
                antwort = new AntwortDTO(
                        antwort.id(),
                        normalized,
                        antwort.frageId(),
                        antwort.studentId(),
                        antwort.antwortZeitpunkt()
                );
            }
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

            if (antwort != null) {
                ReviewDTO review = service.getReviewForAntwort(antwort.id());
                if (review != null) korrektoren.add(review.korrektorId());
                componentList.add(new ReviewAggregateDTO(frage, antwort, review, k));
            } else {
                AntwortDTO emptyAntwort = new AntwortDTO(
                        null,
                        "",
                        frage.id(),
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

                componentList.add(new ReviewAggregateDTO(frage, emptyAntwort, emptyReview, k));
            }
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

            String frageId = String.valueOf(oldDataDTO.fragen().id());
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

    @Override
    public ExamViewForm prepareExamViewForm(UUID examId) {
        ExamDTO exam = service.getExam(examId);
        ProfessorDTO prof = service.getProfessor(exam.professorId());
        List<FrageDTO> fragen = service.getFragenForExam(examId);

        List<ExamAggregateDTO> components = new ArrayList<>();

        double maxPunkte = 0;

        for (FrageDTO frage : fragen) {
            maxPunkte += frage.maxPunkte();
            KorrekteAntwortenDTO k = service.getLoesungForFrage(frage.id());

            if (k != null) {
                components.add(new ExamAggregateDTO(frage, k));
            } else {
                components.add(new ExamAggregateDTO(frage, null));
            }
        }

        return new ExamViewForm(
                exam.title(),
                prof.name(),
                maxPunkte,
                components);
    }
}
