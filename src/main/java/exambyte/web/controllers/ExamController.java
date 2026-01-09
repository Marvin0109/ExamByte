package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.web.form.ExamForm;
import exambyte.web.form.ReviewCoverageForm;
import exambyte.web.form.SubmitForm;
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

    private final ExamControllerHelper helper;

    public  ExamController(ExamControllerHelper helper) {
        this.helper =  helper;
    }

    @GetMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String showCreateExamForm(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();

        ExamForm examForm = helper.createExamForm();

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

        String message = helper.createExam(form, name);

        if (!message.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    message);
            redirectAttributes.addFlashAttribute("success", false);

            return "redirect:/exams/examsProfessoren";
        }

        UUID profFachID = helper.getProfUUID(name);
        UUID examUUID = helper.getExamUUIDByStartTime(form.getStart());

        helper.createQuestions(form, examUUID, profFachID);

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

        List<ExamDTO> examDTOs = helper.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        List<ReviewCoverageForm> covList = helper.getReviewCoverage(examDTOs);

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

        ExamDTO examDTO = helper.getExamByUUID(examFachId);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(examDTO.endTime())) {
            redirectAttributes.addFlashAttribute("message", "Die Prüfung läuft noch! " +
                    "Keine Korrektur erlaubt.");
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/exams/examsKorrektor";
        }

        List<SubmitInfo> submitInfoList = helper.getSubmitInfo(examFachId);

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

        List<ExamDTO> examDTOs = helper.getAllExams();
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
        ExamDTO examDTO = helper.getExamByUUID(examFachId);
        boolean alreadySubmitted = helper.examIsAlreadySubmitted(examFachId, studentLogin);

        UUID author = examDTO.professorFachId();
        String authorIDString = author.toString();

        if (alreadySubmitted) {
            VersuchDTO attempt = helper.getAttempt(examFachId, studentLogin);
            model.addAttribute("attempt", attempt);
        }

        ExamTimeInfo examTimeInfo = helper.getExamTimeInfo(examDTO);

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

        ExamForm form = helper.fillExamForm(examFachId);

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

        boolean submitted = helper.examIsAlreadySubmitted(examFachId, name);

        if (submitted) {
            helper.removeOldAnswersAndReviews(examFachId, name);
        }

        boolean success = helper.submitExam(name, antworten.getAnswers(), examFachId);

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
