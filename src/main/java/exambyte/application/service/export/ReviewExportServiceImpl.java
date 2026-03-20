package exambyte.application.service.export;

import exambyte.application.dto.*;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.domain.export_mapper.ReviewExportDTOMapper;
import exambyte.application.service.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewExportServiceImpl implements ReviewExportService {

    private final ExamQueryService examQueryService;
    private final FrageQueryService frageQueryService;
    private final StudentQueryService studentQueryService;
    private final AnswerQueryService answerQueryService;
    private final ReviewerQueryService reviewerQueryService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewExportDTOMapper mapper;

    public ReviewExportServiceImpl(ExamQueryService examQueryService,
                                   FrageQueryService frageQueryService,
                                   StudentQueryService studentQueryService,
                                   AnswerQueryService answerQueryService,
                                   ReviewerQueryService reviewerQueryService,
                                   ReviewQueryService reviewQueryService,
                                   ReviewExportDTOMapper mapper) {
        this.examQueryService = examQueryService;
        this.frageQueryService = frageQueryService;
        this.studentQueryService = studentQueryService;
        this.answerQueryService = answerQueryService;
        this.reviewerQueryService = reviewerQueryService;
        this.reviewQueryService = reviewQueryService;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewExportDTO> createReviewExport(UUID examId, String studentName) {
        ExamDTO exam = examQueryService.getExam(examId);
        List<FrageDTO> fragen = frageQueryService.getFragenForExam(examId);
        UUID studentId = studentQueryService.getStudentIdByName(studentName);

        List<AnswerDTO> answers = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            AnswerDTO a = answerQueryService.findByStudentAndFrage(studentId, frage.id());
            answers.add(a);
        }

        double maxPunkte = fragen.stream()
                .mapToDouble(FrageDTO::maxPunkte)
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

        return mapper.mapDTOToExport(exam, sb.toString(), maxPunkte, fragen, answers, reviews);
    }
}
