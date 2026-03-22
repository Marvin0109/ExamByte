package exambyte.web.service;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.*;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
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
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamControllerServiceImpl implements ExamControllerService {

    private final ExamFacadeService service;
    private final HelperService helperService;
    private static final double EXAM_COUNT = 12;

    public ExamControllerServiceImpl(ExamFacadeService service, HelperService helperService) {
        this.service = service;
        this.helperService = helperService;
    }

    @Override
    public ExamForm createExamForm(int countQuestions) {
        ExamForm examForm = new ExamForm();

        for (int i = 0; i < countQuestions; i++) {
            QuestionData q = new QuestionData();
            q.setText("");
            q.setType("");
            q.setPoints(0.0);
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
        List<QuestionDTO> fragen = service.getQuestionsForExam(examUUID);

        ExamForm form = new ExamForm();
        form.setStart(examDTO.start());
        form.setEnd(examDTO.end());
        form.setEnd(examDTO.end());
        form.setTitle(examDTO.title());
        form.setId(examUUID);

        List<QuestionData> questions = new ArrayList<>();

        for (QuestionDTO frage : fragen) {
            QuestionData questionData = new QuestionData();
            questionData.setText(frage.text());
            questionData.setPoints(frage.points());
            questionData.setType(frage.type().toString());
            questionData.setId(frage.id());
            if (questionData.getType().equals("MC") || questionData.getType().equals("SC")) {
                String choice = service.getChoicesForQuestion(frage.id());
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
    public List<QuestionDTO> getQuestionForExam(UUID examUUID) {
        return service.getQuestionsForExam(examUUID);
    }

    @Override
    public boolean examIsAlreadySubmitted(UUID examUUID, String studentLogin) {
        return service.isExamAlreadySubmitted(examUUID, studentLogin);
    }

    @Override
    public ProfessorDTO getProfessorById(UUID id) {
        return service.getProfessor(id);
    }

    @Override
    public void createQuestions(ExamForm form, UUID examUUID) {
        for (QuestionData q : form.getQuestions()) {
            String frageText = q.getText();
            frageText = frageText.replace("\\n", "\n");
            QuestionTypeWeb frageTyp;

            try {
                frageTyp = QuestionTypeWeb.valueOf(q.getType().trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Fehlender Fragetyp im ENUM: " + q.getType(), e);
            }

            double maxPunkte = q.getPoints();

            switch(frageTyp) {
                case QuestionTypeWeb.FREE_RESPONSE:
                    service.createQuestion(new QuestionDTO(null, frageText,
                            maxPunkte, examUUID, QuestionTypeDTO.valueOf(frageTyp.name())));
                    break;
                case QuestionTypeWeb.SC:
                    String correctAnswer = q.getCorrectAnswer();
                    QuestionDTO f1 = new QuestionDTO(null, frageText, maxPunkte, examUUID,
                            QuestionTypeDTO.valueOf(frageTyp.name()));
                    service.createChoiceQuestion(f1, correctAnswer, q.getChoices());
                    break;
                case QuestionTypeWeb.MC:
                    String correctAnswers = q.getCorrectAnswers();
                    QuestionDTO f2 = new QuestionDTO(null, frageText, maxPunkte, examUUID,
                            QuestionTypeDTO.valueOf(frageTyp.name()));
                    service.createChoiceQuestion(f2, correctAnswers, q.getChoices());
                    break;
                default:
                    throw new IllegalStateException("Unbehandelter Fragetyp: " + frageTyp);
            }
        }
    }

    @Override
    public AttemptDTO getAttempt(UUID examUUID, String studentLogin) {
        return service.getSubmission(examUUID, studentLogin);
    }

    @Override
    public double getZulassungsProgress(String studentLogin) {
        List<AttemptDTO> allValidAttempts = helperService.getValidAttempts(studentLogin);

        double progressForSuccessAttempt = 100.0 / EXAM_COUNT;
        double progress = 0.0;
        for (AttemptDTO v : allValidAttempts) {
            if (v.erreichtePunkte() >= v.maxPunkte() * 0.5) {
                progress += progressForSuccessAttempt;
            }
        }

        return progress;
    }

    @Override
    public boolean hasAnyFailedAttempt(String studentLogin) {
        List<AttemptDTO> allValidAttempts = helperService.getValidAttempts(studentLogin);

        for (AttemptDTO v : allValidAttempts) {
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
            reviewCoverageList.add(service.reviewCoverage(examDTO.id()));
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
    public boolean submitExam(String name, Map<String, List<String>> answers, UUID examUUID) {
        return service.submitExam(name, answers, examUUID);
    }

    @Override
    public List<SubmitInfo> getSubmitInfo(UUID examUUID) {
        List<StudentDTO> students = service.getStudentSubmittedExam(examUUID);
        List<SubmitInfo> submitInfoList = new ArrayList<>();

        for (StudentDTO student : students) {
            if (service.isSubmitBeingReviewed(examUUID, student.id())) {
                submitInfoList.add(new SubmitInfo(student.name(), student.id(),  true));
            } else {
                submitInfoList.add(new SubmitInfo(student.name(), student.id(), false));
            }
        }

        return submitInfoList;
    }

    @Override
    public void saveAutomaticReviewer() {
        service.saveAutomaticReviewer();
    }

    @Override
    public Optional<UUID> getProfIdByName(String name) {
        return service.getProfIDByName(name);
    }

    @Override
    public boolean reset() {
        return service.reset();
    }

    @Override
    public Map<QuestionDTO, AnswerDTO> getFreeResponseSolutionForExamAndStudent(UUID examId, UUID studentId) {
        List<QuestionDTO> fragen = service.getFreeResponseQuestions(examId);
        List<AnswerDTO> answers = service.getFreeResponseSolutionForExam(examId);

        Map<QuestionDTO, AnswerDTO> resultMap = new HashMap<>();

        for (QuestionDTO frage : fragen) {
            answers.stream()
                    .filter(a -> a.frageId().equals(frage.id()))
                    .filter(a -> a.studentId().equals(studentId))
                    .findFirst().ifPresent(ans -> resultMap.put(frage, ans));
        }

        return resultMap;
    }

    @Override
    public List<AnswerForm> createAnswerForm(Map<QuestionDTO, AnswerDTO> map) {
        List<AnswerForm> answerFormList = new ArrayList<>();

        for (Map.Entry<QuestionDTO, AnswerDTO> entry : map.entrySet()) {
            if (!service.answerHasReview(entry.getValue())) {
                AnswerForm answerForm = new AnswerForm();
                answerForm.setQuestionText(entry.getKey().text());
                answerForm.setAnswer(entry.getValue().answer());
                answerForm.setQuestionPoints(entry.getKey().points());
                answerForm.setAnswerId(entry.getValue().id());

                answerFormList.add(answerForm);
            }
        }

        return answerFormList;
    }

    @Override
    public void createReview(ReviewForm reviewForm, UUID answerId, UUID reviewerId) {
        service.createReview(
                reviewForm.getReviewText(),
                reviewForm.getPoints(),
                answerId,
                reviewerId);
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

    @Override
    public List<ExamExportDTO> getExamExport(UUID examId) {
        return service.getExamExportDTOs(examId);
    }

    @Override
    public List<ReviewExportDTO> getReviewExport(UUID examId, String studentName) {
        return service.getReviewExportDTOs(examId, studentName);
    }

    @Override
    public List<QuestionTypeWeb> createQuestionTypeList(int mcCount, int scCount, int freeResponseCount) {
        List<QuestionTypeWeb> questionTypeWebList = new ArrayList<>();

        questionTypeWebList.addAll(Collections.nCopies(mcCount, QuestionTypeWeb.MC));
        questionTypeWebList.addAll(Collections.nCopies(scCount, QuestionTypeWeb.SC));
        questionTypeWebList.addAll(Collections.nCopies(freeResponseCount, QuestionTypeWeb.FREE_RESPONSE));

        Collections.shuffle(questionTypeWebList);

        return questionTypeWebList;
    }

    @Override
    public ExamViewForm getExamView(UUID examId) {
        return helperService.prepareExamViewForm(examId);
    }
}
