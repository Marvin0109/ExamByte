package exambyte.web.form;

import exambyte.application.dto.ExamDTO;

public class ReviewCoverageForm {

    private final ExamDTO examDTOs;
    private final Double reviewCoverage;

    public ReviewCoverageForm (ExamDTO exam, Double reviewCoverage) {
        this.examDTOs = exam;
        this.reviewCoverage = reviewCoverage;
    }

    public ExamDTO getExam() {
        return examDTOs;
    }

    public Double getReviewCoverage() {
        return reviewCoverage;
    }
}
