package exambyte.domain.model.exam;

public final class ExamCount {
    private static final int MAX_EXAM_COUNT = 12;

    private ExamCount(){}

    public static int getMaxExamCount() {
        return MAX_EXAM_COUNT;
    }
}
