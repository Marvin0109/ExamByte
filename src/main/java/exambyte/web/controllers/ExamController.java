package exambyte.web.controllers;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.service.interfaces.ExamManagementService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamManagementService examManagementService;

    public ExamController(ExamManagementService examManagementService) {
        this.examManagementService = examManagementService;
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

        boolean success = examManagementService.createExam(name, title, startTime, endTime, resultTime);

        if (success) {
            model.addAttribute("message", "Test erfolgreich erstellt!");
        } else {
            model.addAttribute("message", "Fehler beim Erstellen der Prüfung.");
        }
        return "redirect:/exams/create"; // Verhindert doppeltes Absenden
    }

    @GetMapping("/list")
    @Secured("ROLE_STUDENT")
    public String listExams(Model model) {
        List<Exam> exams = examManagementService.getAllExams();
        model.addAttribute("exams", exams);
        return "exams/examsStudierende";
    }

    @GetMapping("/start/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(@PathVariable UUID examFachId, OAuth2AuthenticationToken auth, Model model) {
        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute("login");
        boolean alreadySubmitted = examManagementService.isExamAlreadySubmitted(examFachId, user.getAttribute("login"));

        model.addAttribute("exam", examManagementService.getExam(examFachId));
        model.addAttribute("alreadySubmitted", alreadySubmitted); // Gibt die True oder False ans Formular
        model.addAttribute("name", studentName);
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

        boolean success = examManagementService.submitExam(user.getAttribute("login"), frageFachIds, antwortTexte);

        if (success) {
            model.addAttribute("message", "Alle Antworten erfolgreich eingereicht!");
        } else {
            model.addAttribute("message", "Fehler beim Einreichen der Antworten.");
        }
        return "redirect:/exams/list";
    }
}