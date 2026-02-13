package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamFacadeService;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.create_review.ReviewForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.create_exam.QuestionData;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamControllerServiceImpl implements ExamControllerService {

    private final ExamFacadeService service;
    private final HelperService helperService;

    public ExamControllerServiceImpl(ExamFacadeService service, HelperService helperService) {
        this.service = service;
        this.helperService = helperService;
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
            questionData.setQuestionText(frage.frageText());
            questionData.setPunkte(frage.maxPunkte());
            questionData.setType(frage.type().toString());
            questionData.setFachId(frage.fachId());
            if (questionData.getType().equals("MC") || questionData.getType().equals("SC")) {
                String choice = service.getChoiceForFrage(frage.fachId());
                String normalized = helperService.normalizeAnswerForFrontend(choice);
                questionData.setChoices(normalized);
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
    public ProfessorDTO getProfessorByFachId(UUID fachId) {
        return service.getProfessor(fachId);
    }

    @Override
    public void createQuestions(ExamForm form, UUID profFachID, UUID examUUID) {
        for (QuestionData q : form.getQuestions()) {
            String frageText = q.getQuestionText();
            QuestionTypeWeb frageTyp;

            try {
                frageTyp = QuestionTypeWeb.valueOf(q.getType().trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fehlender Fragetyp im ENUM: " + q.getType(), e);
            }

            int maxPunkte = q.getPunkte();

            switch(frageTyp) {
                case QuestionTypeWeb.FREITEXT:
                    service.createFrage(new FrageDTO(null, frageText,
                            maxPunkte, profFachID, examUUID, QuestionTypeDTO.valueOf(frageTyp.name())));
                    break;
                case QuestionTypeWeb.SC:
                    String correctAnswer = q.getCorrectAnswer();
                    FrageDTO f1 = new FrageDTO(null, frageText, maxPunkte, profFachID, examUUID,
                            QuestionTypeDTO.valueOf(frageTyp.name()));
                    service.createChoiceFrage(f1, correctAnswer, q.getChoices());
                    break;
                case QuestionTypeWeb.MC:
                    String correctAnswers = q.getCorrectAnswers();
                    FrageDTO f2 = new FrageDTO(null, frageText, maxPunkte, profFachID, examUUID,
                            QuestionTypeDTO.valueOf(frageTyp.name()));
                    service.createChoiceFrage(f2, correctAnswers, q.getChoices());
                    break;
                default:
                    throw new IllegalStateException("Unbehandelter Fragetyp: " + frageTyp);
            }
        }
    }

    @Override
    public VersuchDTO getAttempt(UUID examUUID, String studentLogin) {
        return service.getSubmission(examUUID, studentLogin);
    }

    @Override
    public double getZulassungsProgress(String studentLogin) {
        List<VersuchDTO> allValidAttempts = helperService.getValidAttempts(studentLogin);

        double size = 12;
        double progressForSuccessAttempt = 100.0 / size;
        double progress = 0.0;
        for (VersuchDTO v : allValidAttempts) {
            if (v.erreichtePunkte() >= v.maxPunkte() * 0.5) {
                progress += progressForSuccessAttempt;
            }
        }

        return progress;
    }

    @Override
    public boolean hasAnyFailedAttempt(String studentLogin) {
        List<VersuchDTO> allValidAttempts = helperService.getValidAttempts(studentLogin);

        for (VersuchDTO v : allValidAttempts) {
            if (v.erreichtePunkte() < v.maxPunkte() * 0.5) {
                return true;
            }
        }
        return false;
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
        boolean timeLeft = false;
        String fristAnzeige = helperService.getExamAvailabilityNotice(examDTO);
        if (fristAnzeige.isEmpty()) {
            fristAnzeige = helperService.getTimeDifference(examDTO);
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
            if (service.isSubmitBeingReviewed(examUUID, student.fachId())) {
                submitInfoList.add(new SubmitInfo(student.name(), student.fachId(),  true));
            } else {
                submitInfoList.add(new SubmitInfo(student.name(), student.fachId(), false));
            }
        }

        return  submitInfoList;
    }

    @Override
    public void saveAutomaticReviewer() {
        service.saveAutomaticReviewer();
    }

    @Override
    public Optional<UUID> getProfFachIDByName(String name) {
        return service.getProfIDByName(name);
    }

    @Override
    public boolean reset() {
        return service.reset();
    }

    @Override
    public Map<FrageDTO, AntwortDTO> getFreitextAntwortenForExamAndStudent(UUID examFachId, UUID studentFachId) {
        List<FrageDTO> fragen = service.getFreitextFragen(examFachId);
        List<AntwortDTO> antworten = service.getFreitextAntwortenForExam(examFachId);

        Map<FrageDTO, AntwortDTO> resultMap = new HashMap<>();

        for (FrageDTO frage : fragen) {
            antworten.stream()
                    .filter(a -> a.frageFachId().equals(frage.fachId()))
                    .filter(a -> a.studentFachId().equals(studentFachId))
                    .findFirst().ifPresent(ans -> resultMap.put(frage, ans));
        }

        return resultMap;
    }

    @Override
    public List<AnswerForm> createAnswerForm(Map<FrageDTO, AntwortDTO> map) {
        List<AnswerForm> answerFormList = new ArrayList<>();

        for (Map.Entry<FrageDTO, AntwortDTO> entry : map.entrySet()) {
            if (!service.antwortHasReview(entry.getValue())) {
                AnswerForm answerForm = new AnswerForm();
                answerForm.setFrageText(entry.getKey().frageText());
                answerForm.setAntwort(entry.getValue().antwortText());
                answerForm.setMaxPunkte(entry.getKey().maxPunkte());
                answerForm.setAntwortFachId(entry.getValue().fachId());

                answerFormList.add(answerForm);
            }
        }

        return answerFormList;
    }

    @Override
    public void createReview(ReviewForm reviewForm, UUID antwortFachId, UUID korrektorFachId) {
        service.createReview(
                reviewForm.getBewertung(),
                reviewForm.getPunkteVergeben(),
                antwortFachId,
                korrektorFachId);
    }

    @Override
    public UUID getReviewerByName(String name) {
        return service.getReviewerByName(name);
    }

    @Override
    public ReviewViewForm prepareReviewViewForm(UUID examUUID, String studentName) {
        return helperService.prepareReviewViewForm(examUUID, studentName);
    }

    @Override
    public boolean checkTimeForReviewView(UUID examId) {
        return service.timeReachedToViewReview(examId);
    }

    @Override
    public OldDataForm fillOldDataForm(UUID examId, String studentName) {
        return helperService.fillOldDataForm(examId, studentName);
    }

    @Override
    public SubmitForm fillSubmitFormWithData(OldDataForm form) {
        return helperService.fillSubmitFormWithData(form);
    }

    @Override
    public boolean deleteExam(UUID examId) {
        return service.deleteById(examId);
    }
}
