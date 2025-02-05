package exambyte.web.controllers;
import exambyte.domain.aggregate.exam.Antwort;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.domain.aggregate.exam.Frage;
import exambyte.service.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;
    private final AntwortService antwortService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final FrageService frageService;

    public ExamController(ExamService examService, AntwortService antwortService, ProfessorService professorService, StudentService studentService, FrageService frageService) {
        this.examService = examService;
        this.antwortService = antwortService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.frageService = frageService;
    }

    @GetMapping("/create")
    @Secured("ROLE_ADMIN")
    public String showCreateExamForm(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        return "exams/examsProfessoren";
    }

    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public String createExam(
            @RequestParam String title,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam LocalDateTime resultTime,
            Model model, OAuth2AuthenticationToken auth) {

        String name = auth.getPrincipal().getAttribute("login");
        UUID professorFachId = professorService.getProfessorFachId(name);

        Exam newExam = new Exam.ExamBuilder()
                .fachId(null)
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
    @Secured("ROLE_STUDENT")
    public String listExams(Model model) {
        model.addAttribute("exams", examService.alleExams());
        return "exams/examsStudierende";
    }

    @GetMapping("/start/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(@PathVariable UUID examFachId, OAuth2AuthenticationToken auth, Model model) {
        Exam exam = examService.getExam(examFachId);
        OAuth2User user = auth.getPrincipal();
        UUID studentFachId = studentService.getStudentFachId(user.getAttribute("login"));

        List<Frage> fragen = frageService.getFragenForExam(examFachId);

        // Suche nach der bereits abgegebenen Antwort des Studierenden für diese Prüfung
        boolean alreadySubmitted = fragen.stream()
                        .anyMatch(frage -> antwortService.findByStudentAndFrage(studentFachId, frage.getFachId()) != null);

        model.addAttribute("exam", exam);
        model.addAttribute("alreadySubmitted", alreadySubmitted); // Gibt die True oder False ans Formular
        model.addAttribute("name", user.getAttribute("login"));
        return "exams/examsDurchfuehren";
    }


    @PostMapping("/submit")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @RequestParam List<UUID> frageFachIds,
            @RequestParam List<String> antwortTexte,
            OAuth2AuthenticationToken auth,
            Model model) {

        OAuth2User user = auth.getPrincipal();
        UUID studentFachId = studentService.getStudentFachId(user.getAttribute("login"));

        if (frageFachIds.size() != antwortTexte.size()) {
            model.addAttribute("message", "Fehler: Anzahl der Fragen und Antworten stimmt nicht überein.");
            return "redirect:/exams/list";
        }

        List<Antwort> neueAntworten = new ArrayList<>();
        for (int i = 0; i < frageFachIds.size(); i++) {
            UUID frageFachId = frageFachIds.get(i);
            String antwortText = antwortTexte.get(i);

            if (antwortService.findByStudentAndFrage(studentFachId, frageFachId) != null) {
                model.addAttribute("message", "Du hast bereits eine Antwort für eine der Fragen abgegeben.");
                return "redirect:/exams/list";
            }

            Antwort antwort = new Antwort.AntwortBuilder()
                    .fachId(null)
                    .antwortText(antwortText)
                    .frageFachId(frageFachId)
                    .studentFachId(studentFachId)
                    .antwortZeitpunkt(LocalDateTime.now())
                    .lastChangesZeitpunkt(LocalDateTime.now())
                    .build();

            neueAntworten.add(antwort);
        }

        for (Antwort antwort : neueAntworten) {
            antwortService.addAntwort(antwort);
        }

        model.addAttribute("message", "Alle Antworten erfolgreich eingereicht!");
        return "redirect:/exams/list";
    }
}