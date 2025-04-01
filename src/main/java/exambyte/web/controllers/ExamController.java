package exambyte.web.controllers;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.service.ExamManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamManagementService examManagementService;

    @Autowired
    public ExamController(ExamManagementService examManagementService) {
       this.examManagementService = examManagementService;
    }

    @GetMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String showCreateExamForm(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        return "/exams/examsProfessoren";
    }

    // TODO: Mit Thymeleaf Object besser arbeiten
    @PostMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String createExam(
            @RequestParam("title") String title,
            @RequestParam("startTime") LocalDateTime startTime,
            @RequestParam("endTime") LocalDateTime endTime,
            @RequestParam("resultTime") LocalDateTime resultTime,
            Model model, OAuth2AuthenticationToken auth) {

        String name = auth.getPrincipal().getAttribute("login");

        boolean success = examManagementService.createExam(name, title, startTime, endTime, resultTime);

        Optional<UUID> optionalFachId = examManagementService.getProfFachIDByName(name);

        // If Present nicht zu gebrauchen, wird schon in ExamManagementService geprüft
        //noinspection OptionalGetWithoutIsPresent
        UUID profFachID = optionalFachId.get();

        if (success) {
            model.addAttribute("message", "Test erfolgreich erstellt!");
            ExamDTO examDTO = new ExamDTO(null, null, title, profFachID , startTime, endTime, resultTime);
            model.addAttribute("exam", examDTO);
        } else {
            model.addAttribute("message", "Fehler beim Erstellen der Prüfung.");
        }
        return "redirect:exams/examsProfessoren";
    }

    @GetMapping("/examsKorrektor")
    @Secured("ROLE_REVIEWER")
    public String listExamsForReviewer(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsKorrektor";
    }

    @GetMapping("/examStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsStudierende";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(@PathVariable UUID examFachId, OAuth2AuthenticationToken auth, Model model) {
        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute("login");
        boolean alreadySubmitted = examManagementService.isExamAlreadySubmitted(examFachId, user.getAttribute("login"));
        ExamDTO examDTO = examManagementService.getExam(examFachId);

        model.addAttribute("exam", examDTO);
        model.addAttribute("alreadySubmitted", alreadySubmitted); // Gibt die True oder False ans Formular, false wenn Anzahl an Exams (12) erreicht ist
        model.addAttribute("name", studentName);
        return "/exams/examsDurchfuehren";
    }

    // TODO: Thymeleaf Object statt @RequestParam
    @PostMapping("/submit")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @RequestParam List<UUID> frageFachIds,
            @RequestParam List<String> antwortTexte,
            OAuth2AuthenticationToken auth,
            Model model) {

        OAuth2User user = auth.getPrincipal();

        boolean success =
        examManagementService.submitExam(user.getAttribute("login"), frageFachIds, antwortTexte);

        if (success) {
            model.addAttribute("message", "Alle Antworten erfolgreich eingereicht!");
        } else {
            model.addAttribute("message", "Fehler beim Einreichen der Antworten.");
        }
        return "redirect:/exams/list";
    }

    // TODO: unvollständig, Fragetypen fehlen (MC, SC und Freitext)
    @GetMapping("/showExam/{id}")
    @Secured("ROLE_REVIEWER")
    public String showExam(Model model, @PathVariable UUID id) {
        ExamDTO exam = examManagementService.getExam(id);
        List<FrageDTO> fragen = examManagementService.getFragenForExam(id);
        model.addAttribute("exam", exam);
        model.addAttribute("frage", fragen);
        return "/exams/exam";
    }
}
