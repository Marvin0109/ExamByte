package exambyte.application.service.export;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.domain.export_mapper.ExamExportDTOMapper;
import exambyte.application.service.query.ExamQueryService;

import exambyte.application.service.query.FrageQueryService;
import exambyte.application.service.query.CorrectAnswersQueryService;
import exambyte.application.service.query.ProfessorQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamExportServiceImpl implements ExamExportService {

    private final ExamQueryService examQueryService;
    private final FrageQueryService frageQueryService;
    private final ProfessorQueryService professorQueryService;
    private final CorrectAnswersQueryService correctAnswersQueryService;
    private final ExamExportDTOMapper examExportDTOMapper;

    public ExamExportServiceImpl(ExamQueryService examQueryService,
                                 FrageQueryService frageQueryService,
                                 ProfessorQueryService professorQueryService,
                                 CorrectAnswersQueryService correctAnswersQueryService,
                                 ExamExportDTOMapper examExportDTOMapper) {
        this.examQueryService = examQueryService;
        this.frageQueryService = frageQueryService;
        this.professorQueryService = professorQueryService;
        this.correctAnswersQueryService = correctAnswersQueryService;
        this.examExportDTOMapper = examExportDTOMapper;
    }

    @Override
    public List<ExamExportDTO> createExamExport(UUID examId) {
        ExamDTO exam = examQueryService.getExam(examId);
        ProfessorDTO prof = professorQueryService.getProfessorById(exam.professorId());
        List<FrageDTO> fragen = frageQueryService.getFragenForExam(examId);

        double punkte = fragen.stream()
                .mapToDouble(FrageDTO::maxPunkte)
                .sum();

        List<CorrectAnswersDTO> correctAnswersList = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            CorrectAnswersDTO k = correctAnswersQueryService.getSolutionForFrage(frage.id());
            if (k != null) {
                correctAnswersList.add(k);
            }
        }

        return examExportDTOMapper.mapDTOToExport(exam, prof.name(), punkte, fragen, correctAnswersList);
    }
}
