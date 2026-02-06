package exambyte.web.service;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;

import java.util.List;
import java.util.UUID;

public interface HelperService {

    List<VersuchDTO> getValidAttempts(String studentName);

    String getExamAvailabilityNotice(ExamDTO dto);

    String getTimeDifference(ExamDTO dto);

    String normalizeAnswerForFrontend(String toSplit);

    PreparedFrageData prepareFrageData(FrageDTO frage, UUID studentId);

    ReviewViewForm prepareReviewViewForm(UUID examId, String studentName);

    OldDataForm fillOldDataForm(UUID examId, String studentName);

    SubmitForm fillSubmitFormWithData(OldDataForm form);
}
