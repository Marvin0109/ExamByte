package exambyte.application.service.export;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.domain.export_mapper.ExamExportDTOMapper;
import exambyte.application.service.query.ExamQueryService;

import exambyte.application.service.query.QuestionQueryService;
import exambyte.application.service.query.CorrectAnswersQueryService;
import exambyte.application.service.query.ProfessorQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamExportServiceImpl implements ExamExportService {

    private final ExamQueryService examQueryService;
    private final QuestionQueryService questionQueryService;
    private final ProfessorQueryService professorQueryService;
    private final CorrectAnswersQueryService correctAnswersQueryService;
    private final ExamExportDTOMapper examExportDTOMapper;

    public ExamExportServiceImpl(ExamQueryService examQueryService,
                                 QuestionQueryService questionQueryService,
                                 ProfessorQueryService professorQueryService,
                                 CorrectAnswersQueryService correctAnswersQueryService,
                                 ExamExportDTOMapper examExportDTOMapper) {
        this.examQueryService = examQueryService;
        this.questionQueryService = questionQueryService;
        this.professorQueryService = professorQueryService;
        this.correctAnswersQueryService = correctAnswersQueryService;
        this.examExportDTOMapper = examExportDTOMapper;
    }

    @Override
    public List<ExamExportDTO> createExamExport(UUID examId) {
        ExamDTO exam = examQueryService.getExam(examId);
        ProfessorDTO prof = professorQueryService.getProfessorById(exam.professorId());
        List<QuestionDTO> questions = questionQueryService.getQuestionsForExam(examId);

        double punkte = questions.stream()
                .mapToDouble(QuestionDTO::points)
                .sum();

        List<CorrectAnswersDTO> correctAnswersList = new ArrayList<>();

        for (QuestionDTO frage : questions) {
            CorrectAnswersDTO k = correctAnswersQueryService.getSolutionForFrage(frage.id());
            if (k != null) {
                correctAnswersList.add(k);
            }
        }

        return examExportDTOMapper.mapDTOToExport(exam, prof.name(), punkte, questions, correctAnswersList);
    }
}
