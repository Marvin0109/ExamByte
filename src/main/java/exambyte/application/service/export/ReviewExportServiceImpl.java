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
    private final AntwortQueryService antwortQueryService;
    private final ReviewerQueryService reviewerQueryService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewExportDTOMapper mapper;

    public ReviewExportServiceImpl(ExamQueryService examQueryService,
                                   FrageQueryService frageQueryService,
                                   StudentQueryService studentQueryService,
                                   AntwortQueryService antwortQueryService,
                                   ReviewerQueryService reviewerQueryService,
                                   ReviewQueryService reviewQueryService,
                                   ReviewExportDTOMapper mapper) {
        this.examQueryService = examQueryService;
        this.frageQueryService = frageQueryService;
        this.studentQueryService = studentQueryService;
        this.antwortQueryService = antwortQueryService;
        this.reviewerQueryService = reviewerQueryService;
        this.reviewQueryService = reviewQueryService;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewExportDTO> createReviewExport(UUID examId, String studentName) {
        ExamDTO exam = examQueryService.getExam(examId);
        List<FrageDTO> fragen = frageQueryService.getFragenForExam(examId);
        UUID studentId = studentQueryService.getStudentIdByName(studentName);

        List<AntwortDTO> antworten = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            AntwortDTO a = antwortQueryService.findByStudentAndFrage(studentId, frage.id());
            antworten.add(a);
        }

        double maxPunkte = fragen.stream()
                .mapToDouble(FrageDTO::maxPunkte)
                .sum();

        List<ReviewDTO> reviews = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (AntwortDTO antwort : antworten) {
            ReviewDTO r = reviewQueryService.getReviewByAntwortId(antwort.id());

            if (r != null) {
                reviews.add(r);
                ReviewerDTO reviewerDTO = reviewerQueryService.getReviewerById(r.reviewerId());

                if (!reviewerDTO.name().equals("Automatischer Reviewer")) {

                    if (!sb.isEmpty()) {
                        sb.append(", ");
                    }

                    sb.append(reviewerDTO.name());
                }
            }
        }

        return mapper.mapDTOToExport(exam, sb.toString(), maxPunkte, fragen, antworten, reviews);
    }
}
