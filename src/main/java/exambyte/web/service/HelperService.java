package exambyte.web.service;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.AttemptDTO;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;

import java.util.List;
import java.util.UUID;

public interface HelperService {

    List<AttemptDTO> getValidAttempts(String studentName);

    String getExamAvailabilityNotice(ExamDTO dto);

    String getTimeDifference(ExamDTO dto);

    String normalizeAnswerForFrontend(String toSplit);

    PreparedQuestionData prepareFrageData(QuestionDTO frage, UUID studentId);

    ReviewViewForm prepareReviewViewForm(UUID examId, String studentName);

    OldDataForm fillOldDataForm(UUID examId, String studentName);

    SubmitForm fillSubmitFormWithData(OldDataForm form);

    ExamViewForm prepareExamViewForm(UUID examId);
}
