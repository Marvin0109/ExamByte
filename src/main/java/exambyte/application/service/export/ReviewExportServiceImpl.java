package exambyte.application.service.export;

import exambyte.application.dto.*;
import exambyte.application.dto.export.ReviewExportDTO;
import exambyte.application.mapper.export.ReviewExportDTOMapper;
import exambyte.application.service.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewExportServiceImpl implements ReviewExportService {

    private final ExamService examService;
    private final QuestionService questionService;
    private final StudentService studentService;
    private final AnswerService answerService;
    private final ReviewerService reviewerService;
    private final ReviewService reviewService;
    private final ReviewExportDTOMapper mapper;

    public ReviewExportServiceImpl(ExamService examService,
                                   QuestionService questionService,
                                   StudentService studentService,
                                   AnswerService answerService,
                                   ReviewerService reviewerService,
                                   ReviewService reviewService,
                                   ReviewExportDTOMapper mapper) {
        this.examService = examService;
        this.questionService = questionService;
        this.studentService = studentService;
        this.answerService = answerService;
        this.reviewerService = reviewerService;
        this.reviewService = reviewService;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewExportDTO> createReviewExport(UUID examId, String studentName) {
        ExamDTO exam = examService.getExam(examId);
        List<QuestionDTO> questions = questionService.getQuestionsForExam(examId);
        UUID studentId = studentService.getStudentIdByName(studentName);

        List<AnswerDTO> answers = new ArrayList<>();

        for (QuestionDTO question : questions) {
            AnswerDTO a = answerService.findByStudentAndQuestion(studentId, question.id());
            answers.add(a);
        }

        double points = questions.stream()
                .mapToDouble(QuestionDTO::points)
                .sum();

        List<ReviewDTO> reviews = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (AnswerDTO answer : answers) {
            ReviewDTO r = reviewService.getReviewByAnswerId(answer.id());

            if (r != null) {
                reviews.add(r);
                ReviewerDTO reviewerDTO = reviewerService.getReviewerById(r.reviewerId());

                if (!reviewerDTO.name().equals("Auto reviewer")) {

                    if (!sb.isEmpty()) {
                        sb.append(", ");
                    }

                    sb.append(reviewerDTO.name());
                }
            }
        }

        return mapper.mapDTOToExport(exam, sb.toString(), points, questions, answers, reviews);
    }
}
