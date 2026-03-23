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

    private final ExamQueryService examQueryService;
    private final QuestionQueryService questionQueryService;
    private final StudentQueryService studentQueryService;
    private final AnswerService answerService;
    private final ReviewerQueryService reviewerQueryService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewExportDTOMapper mapper;

    public ReviewExportServiceImpl(ExamQueryService examQueryService,
                                   QuestionQueryService questionQueryService,
                                   StudentQueryService studentQueryService,
                                   AnswerService answerService,
                                   ReviewerQueryService reviewerQueryService,
                                   ReviewQueryService reviewQueryService,
                                   ReviewExportDTOMapper mapper) {
        this.examQueryService = examQueryService;
        this.questionQueryService = questionQueryService;
        this.studentQueryService = studentQueryService;
        this.answerService = answerService;
        this.reviewerQueryService = reviewerQueryService;
        this.reviewQueryService = reviewQueryService;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewExportDTO> createReviewExport(UUID examId, String studentName) {
        ExamDTO exam = examQueryService.getExam(examId);
        List<QuestionDTO> questions = questionQueryService.getQuestionsForExam(examId);
        UUID studentId = studentQueryService.getStudentIdByName(studentName);

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
            ReviewDTO r = reviewQueryService.getReviewByAnswerId(answer.id());

            if (r != null) {
                reviews.add(r);
                ReviewerDTO reviewerDTO = reviewerQueryService.getReviewerById(r.reviewerId());

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
