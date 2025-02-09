package exambyte.infrastructure.service;

import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.domain.service.ExamManagementService;
import exambyte.domain.repository.ExamRepository;
import exambyte.domain.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamManagementServiceImpl implements ExamManagementService {

    private final ExamService examService;
    private final ExamRepository examRepository;
    private final AntwortService antwortService;
    private final FrageService frageService;
    private final StudentService studentService;
    private final ProfessorService professorService;

    public ExamManagementServiceImpl(ExamService examService, AntwortService antwortService, FrageService frageService,
                                     StudentService studentService, ProfessorService professorService, ExamRepository examRepository) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.frageService = frageService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.examRepository = examRepository;
    }

    @Override
    public boolean createExam(String professorName, String title, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        UUID profFachId = null;
        Optional<UUID> optionalFachID = professorService.getProfessorFachId(professorName);
        if (optionalFachID.isPresent()) {
            profFachId = optionalFachID.get();
        }
        examService.addExam(null, null, title, profFachId, startTime, endTime, resultTime);
        return true;
    }

    @Override
    public List<Exam> getAllExams() {
        return examService.alleExams();  // Hole Domain-Objekte
    }

    @Override
    public boolean isExamAlreadySubmitted(UUID examFachId, String studentName) {
        UUID studentFachId = studentService.getStudentFachId(studentName);
        List<Frage> fragen = frageService.getFragenForExam(examFachId);

        return fragen.stream()
                .anyMatch(frage -> antwortService.findByStudentAndFrage(studentFachId, frage.getFachId()) != null);
    }

    @Override
    public boolean submitExam(String studentLogin, List<UUID> frageFachIds, List<String> antwortTexte) {
        UUID studentFachId = studentService.getStudentFachId(studentLogin);

        if (frageFachIds.size() != antwortTexte.size()) {
            return false;
        }

        for (int i = 0; i < frageFachIds.size(); i++) {
            UUID frageFachId = frageFachIds.get(i);
            String antwortText = antwortTexte.get(i);

            if (antwortService.findByStudentAndFrage(studentFachId, frageFachId) != null) {
                return false;
            }

            Antwort antwort = new Antwort.AntwortBuilder()
                    .fachId(null)
                    .antwortText(antwortText)
                    .frageFachId(frageFachId)
                    .studentFachId(studentFachId)
                    .antwortZeitpunkt(LocalDateTime.now())
                    .lastChangesZeitpunkt(LocalDateTime.now())
                    .build();

            antwortService.addAntwort(antwort);
        }

        return true;
    }

    @Override
    public Exam getExam(UUID examFachId) {
        return examRepository.findByFachId(examFachId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }
}
