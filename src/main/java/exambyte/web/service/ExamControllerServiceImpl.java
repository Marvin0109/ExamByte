package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExamControllerServiceImpl implements ExamControllerService {

    private final ExamManagementService service;

    public ExamControllerServiceImpl(ExamManagementService service) {
        this.service = service;
    }

    @Override
    public ExamForm createExamForm() {
        ExamForm examForm = new ExamForm();

        for (int i = 0; i < 6; i++) {
            QuestionData q = new QuestionData();
            q.setQuestionText("");
            q.setType("");
            q.setPunkte(0);
            q.setChoices("");
            q.setCorrectAnswers("");
            q.setCorrectAnswer("");
            examForm.getQuestions().add(q);
        }

        return examForm;
    }

    @Override
    public ExamForm fillExamForm(UUID examUUID) {
        ExamDTO examDTO = service.getExam(examUUID);
        List<FrageDTO> fragen = service.getFragenForExam(examUUID);

        ExamForm form = new ExamForm();
        form.setStart(examDTO.startTime());
        form.setEnd(examDTO.endTime());
        form.setEnd(examDTO.endTime());
        form.setTitle(examDTO.title());
        form.setFachId(examUUID);

        List<QuestionData> questions = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            QuestionData questionData = new QuestionData();
            questionData.setQuestionText(frage.getFrageText());
            questionData.setPunkte(frage.getMaxPunkte());
            questionData.setType(frage.getType().toString());
            questionData.setFachId(frage.getFachId());
            if (questionData.getType().equals("MC") || questionData.getType().equals("SC")) {
                String choice = service.getChoiceForFrage(frage.getFachId());
                questionData.setChoices(choice.replace("\n", ","));
                System.out.println(questionData.getChoices());
            }
            questions.add(questionData);
        }

        form.setQuestions(questions);

        return form;
    }

    @Override
    public UUID getExamUUIDByStartTime(LocalDateTime startTime) {
        return service.getExamByStartTime(startTime);
    }

    @Override
    public String createExam(ExamForm form, String name) {
        return service.createExam(
                name,
                form.getTitle(),
                form.getStart(),
                form.getEnd(),
                form.getResult()
        );
    }

    @Override
    public ExamDTO getExamByUUID(UUID examUUID) {
        return service.getExam(examUUID);
    }

    @Override
    public List<ExamDTO> getAllExams() {
        return service.getAllExams();
    }

    @Override
    public List<FrageDTO> getFragenForExam(UUID examUUID) {
        return service.getFragenForExam(examUUID);
    }

    @Override
    public boolean examIsAlreadySubmitted(UUID examUUID, String studentLogin) {
        return service.isExamAlreadySubmitted(examUUID, studentLogin);
    }

    @Override
    public UUID getProfUUID(String name) {
        Optional<UUID> profUUID = service.getProfFachIDByName(name);
        if (profUUID.isPresent()) {
            return profUUID.get();
        } else {
            throw new NichtVorhandenException();
        }
    }

    @Override
    public void createQuestions(ExamForm form, UUID profFachID, UUID examUUID) {
        for (QuestionData q : form.getQuestions()) {
            String frageText = q.getQuestionText();
            QuestionTypeWeb frageTyp = QuestionTypeWeb.valueOf(q.getType().trim());
            int maxPunkte = q.getPunkte();

            switch(frageTyp) {
                case QuestionTypeWeb.FREITEXT:
                    service.createFrage(new FrageDTO(null, null, frageText,
                            maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()), profFachID, examUUID));
                    break;
                case QuestionTypeWeb.SC:
                    String correctAnswer = q.getCorrectAnswer();
                    FrageDTO f1 = new FrageDTO(null, null, frageText, maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()),
                            profFachID, examUUID);
                    service.createChoiceFrage(f1, new KorrekteAntwortenDTO(null, null,
                            f1.getFachId(), correctAnswer, q.getChoices()));
                    break;
                case QuestionTypeWeb.MC:
                    String correctAnswers = q.getCorrectAnswers();
                    FrageDTO f2 = new FrageDTO(null, null, frageText, maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()),
                            profFachID, examUUID);
                    service.createChoiceFrage(f2, new KorrekteAntwortenDTO(null, null, f2.getFachId(),
                            correctAnswers, q.getChoices()));
                    break;
                default:
            }
        }
    }

    @Override
    public VersuchDTO getAttempt(UUID examUUID, String studentLogin) {
        return service.getSubmission(examUUID, studentLogin);
    }

    @Override
    public List<ReviewCoverageForm> getReviewCoverage(List<ExamDTO> examDTOList) {
        List<Double> reviewCoverageList = new ArrayList<>();
        for (ExamDTO examDTO : examDTOList) {
            reviewCoverageList.add(service.reviewCoverage(examDTO.fachId()));
        }

        List<ReviewCoverageForm> covList = new ArrayList<>();

        for (int i = 0; i < examDTOList.size(); i++) {
            ReviewCoverageForm rcf = new ReviewCoverageForm(examDTOList.get(i), reviewCoverageList.get(i));
            covList.add(rcf);
        }

        return covList;
    }

    @Override
    public ExamTimeInfo getExamTimeInfo(ExamDTO examDTO) {
        String fristAnzeige = "";
        String tageAnzeige = "";
        String stundenAnzeige = "";
        String minutenAnzeige = "";
        boolean timeLeft = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy, HH:mm");
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(examDTO.startTime())) {
            String startTimeFormatted = examDTO.startTime().format(formatter);

            fristAnzeige = "Der Test kann erst ab den " + startTimeFormatted + " bearbeitet werden.";
        } else if (now.isAfter(examDTO.endTime())) {
            String endTimeFormatted = examDTO.endTime().format(formatter);

            fristAnzeige = "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten. Der Test " +
                    "konnte nur bis " + endTimeFormatted + " bearbeitet werden.";
        } else {
            Duration diff = Duration.between(LocalDateTime.now(), examDTO.endTime());

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

            if (!tageAnzeige.isEmpty()) {
                fristAnzeige += tageAnzeige + " ";
            }
            if (!stundenAnzeige.isEmpty()) {
                fristAnzeige += stundenAnzeige + " ";
            }
            if (!minutenAnzeige.isEmpty()) {
                fristAnzeige += minutenAnzeige;
            }

            timeLeft = true;
        }

        return new ExamTimeInfo(fristAnzeige, timeLeft);
    }

    @Override
    public void removeOldAnswersAndReviews(UUID examUUID, String name) {
        service.removeOldAnswers(examUUID, name);
    }

    @Override
    public boolean submitExam(String name, Map<String, List<String>> answers, UUID examUUID) {
        return service.submitExam(name, answers, examUUID);
    }

    @Override
    public List<SubmitInfo> getSubmitInfo(UUID examUUID) {
        List<StudentDTO> students = service.getStudentSubmittedExam(examUUID);
        List<SubmitInfo> submitInfoList = new ArrayList<>();

        for (StudentDTO student : students) {
            if (service.isSubmitBeingReviewed(examUUID, student)) {
                submitInfoList.add(new SubmitInfo(student.name(), student.fachId(),  true));
            } else {
                submitInfoList.add(new SubmitInfo(student.name(), student.fachId(), false));
            }
        }

        return  submitInfoList;
    }
}
