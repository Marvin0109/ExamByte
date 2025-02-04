package exambyte.web.controllers;
import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.service.AntwortService;
import exambyte.service.ExamService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;
    private final AntwortService antwortService;

    public ExamController(ExamService examService, AntwortService antwortService) {
        this.examService = examService;
        this.antwortService = antwortService;
    }

    /**
     * Zeigt die Prüfungsseite an, aber nur für Benutzer mit der Rolle "ROLE_ADMIN".
     * Der Zugriff wird durch die {@link Secured}-Annotation geschützt.
     *
     * @param model Das Model, um Daten an die View zu übergeben.
     * @return Der Name der View für die Prüfungsseite.
    @GetMapping("/exams")
    @Secured("ROLE_ADMIN")
    public String exams(Model model, OAuth2AuthenticationToken auth) {
        System.out.println(auth);
        System.out.println("Entered Exams");

        model.addAttribute("exams", examService.alleExams());
        return "exams";
    }*/

    @GetMapping("/create")
    public String showCreateExamForm() {
        return "exams/examsProfessoren";
    }
    @PostMapping("/create")
    public String createExam(
            @RequestParam String title,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam LocalDateTime resultTime,
            Model model) {

        UUID professorFachId = UUID.randomUUID();

        Exam newExam = new Exam.ExamBuilder()
                .fachId(UUID.randomUUID())
                .title(title)
                .professorFachId(professorFachId)
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        examService.addExam(newExam);

        model.addAttribute("message", "Test erfolgreich erstellt!");
        return "redirect:/exams/create"; // Verhindert doppeltes Absenden
    }

    @GetMapping("/list")
    public String listExams(Model model) {
        model.addAttribute("exams", examService.alleExams());
        return "exams/examsStudierende";
    }

    @GetMapping("/start/{id}")
    public String startExam(@PathVariable UUID id, OAuth2AuthenticationToken auth, Model model) {
        Exam exam = examService.getExam(id);
        UUID studentId = UUID.fromString(auth.getPrincipal().getAttribute("sub"));

        // Suche nach der bereits abgegebenen Antwort des Studierenden für diese Prüfung
        Antwort existingAntwort = antwortService.findByStudentAndExam(studentId, id);

        model.addAttribute("exam", exam);
        model.addAttribute("antwort", existingAntwort); // Gibt die Antwort an das Formular weiter
        return "exams/examsDurchfuehren";
    }


    @PostMapping("/submit")
    public String submitExam(
            @RequestParam UUID examId,
            @RequestParam String antwortText,
            OAuth2AuthenticationToken auth,
            Model model) {

        // Holen des studentId aus dem Auth-Token (OAuth2)
        UUID studentId = UUID.fromString(auth.getPrincipal().getAttribute("sub"));

        // Prüfen, ob der Studierende bereits eine Antwort abgegeben hat
        Antwort existingAntwort = antwortService.findByStudentAndExam(studentId, examId);
        if (existingAntwort != null) {
            model.addAttribute("message", "Du hast bereits eine Antwort für diese Prüfung abgegeben.");
            return "redirect:/exams/list";
        }

        // Erstellen einer neuen Antwort
        Antwort antwort = new Antwort.AntwortBuilder()
                .fachId(examId)
                .antwortText(antwortText)
                .frageFachId(examId)
                .studentFachId(studentId)
                .antwortZeitpunkt(LocalDateTime.now())
                .build();

        // Speichern der Antwort in der Datenbank
        antwortService.addAntwort(antwort);

        model.addAttribute("message", "Antwort erfolgreich eingereicht!");
        return "redirect:/exams/list";
    }
}