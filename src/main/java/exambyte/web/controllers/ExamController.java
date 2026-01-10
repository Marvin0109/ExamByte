package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.web.form.*;
import exambyte.application.service.ExamControllerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamControllerService service;

    public  ExamController(ExamControllerService service) {
        this.service =  service;
    }

    @GetMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String showCreateExamForm(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();

        ExamForm examForm = service.createExamForm();

        model.addAttribute("name", user.getAttribute("login"));
        model.addAttribute("examForm", examForm);
        model.addAttribute("currentPath", request.getRequestURI());
        return "/exams/examsProfessoren";
    }

    @PostMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String createExam(
            @Valid ExamForm form,
            BindingResult bindingResult,
            Model model,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Fehlerhafte Eingabedaten!");
            redirectAttributes.addFlashAttribute("success", false);

            return "redirect:/exams/examsProfessoren";
        }

        if (form.getQuestions().size() < 6){
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Weniger Fragen als sonst.");
            redirectAttributes.addFlashAttribute("success", false);

            return "redirect:/exams/examsProfessoren";
        }

        String name = auth.getPrincipal().getAttribute("login");

        String message = service.createExam(form, name);

        if (!message.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    message);
            redirectAttributes.addFlashAttribute("success", false);

            return "redirect:/exams/examsProfessoren";
        }

        UUID profFachID = service.getProfUUID(name);
        UUID examUUID = service.getExamUUIDByStartTime(form.getStart());

        service.createQuestions(form, examUUID, profFachID);

        redirectAttributes.addFlashAttribute(
                "message",
                "Prüfung und Fragen erfolgreich erstellt!");
        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/exams/examsProfessoren";
    }

    @GetMapping("/examsKorrektor")
    @Secured("ROLE_REVIEWER")
    public String listExamsForReviewer(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        List<ReviewCoverageForm> covList = service.getReviewCoverage(examDTOs);

        model.addAttribute("reviewCoverage", covList);
        model.addAttribute("timeNow", now);
        model.addAttribute("currentPath", request.getRequestURI());
        return "/exams/examsKorrektor";
    }

    @GetMapping("/showExamSubmits/{examFachId}")
    @Secured("ROLE_REVIEWER")
    public String showExamSubmits(
            Model model,
            @PathVariable UUID examFachId,
            RedirectAttributes redirectAttributes) {

        ExamDTO examDTO = service.getExamByUUID(examFachId);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(examDTO.endTime())) {
            redirectAttributes.addFlashAttribute("message", "Die Prüfung läuft noch! " +
                    "Keine Korrektur erlaubt.");
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/exams/examsKorrektor";
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examFachId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", examDTO);
        model.addAttribute("timeNow", now);
        return "/exams/examSubmittedUebersicht";
    }

    //TODO: Korrektur Seite, HTML existiert noch nicht
    @GetMapping("/showSubmit/{examFachId}/{studentFachId}")
    @Secured("ROLE_REVIEWER")
    public String showSubmit(
            Model model,
            @PathVariable UUID examFachId,
            @PathVariable UUID studentFachId) {

        return "/exams/showSubmit";
    }

    @GetMapping("/examsStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute("login");

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        model.addAttribute("timeNow", now);
        model.addAttribute("exams", examDTOs);
        model.addAttribute("name", studentName);
        model.addAttribute("currentPath", request.getRequestURI());
        return "/exams/examsStudierende";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}/menu")
    @Secured("ROLE_STUDENT")
    public String examMenu(
            @PathVariable UUID examFachId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String studentLogin = user.getAttribute("login");
        ExamDTO examDTO = service.getExamByUUID(examFachId);
        boolean alreadySubmitted = service.examIsAlreadySubmitted(examFachId, studentLogin);

        UUID author = examDTO.professorFachId();
        String authorIDString = author.toString();

        if (alreadySubmitted) {
            VersuchDTO attempt = service.getAttempt(examFachId, studentLogin);
            model.addAttribute("attempt", attempt);
        }

        ExamTimeInfo examTimeInfo = service.getExamTimeInfo(examDTO);

        model.addAttribute("exam", examDTO);
        model.addAttribute("alreadySubmitted", alreadySubmitted);
        model.addAttribute("timeLeft", examTimeInfo.fristAnzeige());
        model.addAttribute("timeLeftBool", examTimeInfo.timeLeft());

        //TODO: For better UX: Show author name instead
        model.addAttribute("authorID", authorIDString);
        return "/exams/examMenu";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(
            @PathVariable UUID examFachId,
            Model model) {

        ExamForm form = service.fillExamForm(examFachId);

        SubmitForm submitForm = new SubmitForm();

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "/exams/examsDurchfuehren";
    }

    @PostMapping("/submit/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @PathVariable UUID examFachId,
            @ModelAttribute SubmitForm antworten,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        OAuth2User user = auth.getPrincipal();
        String name =  user.getAttribute("login");

        boolean submitted = service.examIsAlreadySubmitted(examFachId, name);

        if (submitted) {
            service.removeOldAnswersAndReviews(examFachId, name);
        }

        boolean success = service.submitExam(name, antworten.getAnswers(), examFachId);

        if (success) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Alle Antworten erfolgreich eingereicht!");
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Fehler beim Einreichen der Antworten.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/exams/examsStudierende";
    }
}
