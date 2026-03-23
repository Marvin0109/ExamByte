package exambyte.application.service.export;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.dto.CorrectAnswersDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.export.ExamExportDTO;
import exambyte.application.mapper.export.ExamExportDTOMapper;
import exambyte.application.service.query.ExamQueryService;

import exambyte.application.service.query.QuestionQueryService;
import exambyte.application.service.query.CorrectAnswersQueryService;
import exambyte.application.service.query.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamExportServiceImpl implements ExamExportService {

    private final ExamQueryService examQueryService;
    private final QuestionQueryService questionQueryService;
    private final ProfessorService professorService;
    private final CorrectAnswersQueryService correctAnswersQueryService;
    private final ExamExportDTOMapper examExportDTOMapper;

    public ExamExportServiceImpl(ExamQueryService examQueryService,
                                 QuestionQueryService questionQueryService,
                                 ProfessorService professorService,
                                 CorrectAnswersQueryService correctAnswersQueryService,
                                 ExamExportDTOMapper examExportDTOMapper) {
        this.examQueryService = examQueryService;
        this.questionQueryService = questionQueryService;
        this.professorService = professorService;
        this.correctAnswersQueryService = correctAnswersQueryService;
        this.examExportDTOMapper = examExportDTOMapper;
    }

    @Override
    public List<ExamExportDTO> createExamExport(UUID examId) {
        ExamDTO exam = examQueryService.getExam(examId);
        ProfessorDTO prof = professorService.getProfessorById(exam.professorId());
        List<QuestionDTO> questions = questionQueryService.getQuestionsForExam(examId);

        double points = questions.stream()
                .mapToDouble(QuestionDTO::points)
                .sum();

        List<CorrectAnswersDTO> correctAnswersList = new ArrayList<>();

        for (QuestionDTO question : questions) {
            CorrectAnswersDTO k = correctAnswersQueryService.getCorrectAnswerForQuestion(question.id());
            if (k != null) {
                correctAnswersList.add(k);
            }
        }

        return examExportDTOMapper.mapDTOToExport(exam, prof.name(), points, questions, correctAnswersList);
    }
}
