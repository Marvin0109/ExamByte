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
    private final KorrektorQueryService korrektorQueryService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewExportDTOMapper mapper;

    public ReviewExportServiceImpl(ExamQueryService examQueryService,
                                   FrageQueryService frageQueryService,
                                   StudentQueryService studentQueryService,
                                   AntwortQueryService antwortQueryService,
                                   KorrektorQueryService korrektorQueryService,
                                   ReviewQueryService reviewQueryService,
                                   ReviewExportDTOMapper mapper) {
        this.examQueryService = examQueryService;
        this.frageQueryService = frageQueryService;
        this.studentQueryService = studentQueryService;
        this.antwortQueryService = antwortQueryService;
        this.korrektorQueryService = korrektorQueryService;
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
            AntwortDTO a = antwortQueryService.findByStudentAndFrage(studentId, frage.fachId());

            if (a != null) {
                antworten.add(a);
            }
        }

        int maxPunkte = fragen.stream()
                .map(FrageDTO::maxPunkte)
                .reduce(0, Integer::sum);

        List<ReviewDTO> reviews = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (AntwortDTO antwort : antworten) {
            ReviewDTO r = reviewQueryService.getReviewByAntwortId(antwort.fachId());

            if (r != null) {
                reviews.add(r);
                KorrektorDTO korrektorDTO = korrektorQueryService.getReviewerById(r.korrektorFachId());

                if (!korrektorDTO.name().equals("Automatischer Korrektor")) {

                    if (!sb.isEmpty()) {
                        sb.append(", ");
                    }

                    sb.append(korrektorDTO.name());
                }
            }
        }

        return mapper.mapDTOToExport(exam, sb.toString(), maxPunkte, fragen, antworten, reviews);
    }
}
