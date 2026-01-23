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
    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String MESSAGE = "message";
    private static final String SUCCESS = "success";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_EXAM_PROF = "redirect:/exams/examsProfessoren";

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
        String name = user.getAttribute(LOGIN_NAME);

        ExamForm examForm = service.createExamForm();

        model.addAttribute("name", name);
        model.addAttribute("examForm", examForm);
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return "exams/examsProfessoren";
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
            return redirectWithMessage(
                    redirectAttributes,
                    "Fehlerhafte Eingabedaten!",
                    false);
        }

        if (form.getQuestions().size() < 6){
            return redirectWithMessage(
                    redirectAttributes,
                    "Weniger Fragen als sonst.",
                    false);
        }

        String name = auth.getPrincipal().getAttribute(LOGIN_NAME);
        Optional<UUID> profFachID = service.getProfFachIDByName(name);
        UUID fachId = null;
        if (profFachID.isPresent()) {
            fachId = profFachID.get();
        }

        String message = service.createExam(form, name);

        if (!message.isEmpty()) {
            return redirectWithMessage(
                    redirectAttributes,
                    message,
                    false);
        }

        UUID examUUID = service.getExamUUIDByStartTime(form.getStart());

        service.createQuestions(form, fachId, examUUID);

        return redirectWithMessage(
                redirectAttributes,
                "Prüfung und Fragen erfolgreich erstellt!",
                true);
    }

    private String redirectWithMessage(RedirectAttributes redirectAttributes, String message, boolean success) {
        redirectAttributes.addFlashAttribute(MESSAGE, message);
        redirectAttributes.addFlashAttribute(SUCCESS, success);
        return REDIRECT_EXAM_PROF;
    }

    @GetMapping("/examsKorrektor")
    @Secured("ROLE_REVIEWER")
    public String listExamsForReviewer(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute(LOGIN_NAME));

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        List<ReviewCoverageForm> covList = service.getReviewCoverage(examDTOs);

        model.addAttribute("reviewCoverage", covList);
        model.addAttribute(TIME_NOW, now);
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return "exams/examsKorrektor";
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
            redirectAttributes.addFlashAttribute(MESSAGE, "Die Prüfung läuft noch! " +
                    "Keine Korrektur erlaubt.");
            redirectAttributes.addFlashAttribute(SUCCESS, false);
            return "redirect:/exams/examsKorrektor";
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examFachId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", examDTO);
        model.addAttribute(TIME_NOW, now);
        return "exams/examSubmittedUebersicht";
    }

    //TODO: Korrektur Seite, HTML existiert noch nicht
    @GetMapping("/showSubmit/{examFachId}/{studentFachId}")
    @Secured("ROLE_REVIEWER")
    public String showSubmit(
            Model model,
            @PathVariable UUID examFachId,
            @PathVariable UUID studentFachId) {

        return "exams/showSubmit";
    }

    @GetMapping("/examsStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute(LOGIN_NAME);

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        model.addAttribute(TIME_NOW, now);
        model.addAttribute("exams", examDTOs);
        model.addAttribute("name", studentName);
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return "exams/examsStudierende";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}/menu")
    @Secured("ROLE_STUDENT")
    public String examMenu(
            @PathVariable UUID examFachId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String studentLogin = user.getAttribute(LOGIN_NAME);
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
        return "exams/examMenu";
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
        return "exams/examsDurchfuehren";
    }

    @PostMapping("/submit/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @PathVariable UUID examFachId,
            @ModelAttribute SubmitForm antworten,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        OAuth2User user = auth.getPrincipal();
        String name =  user.getAttribute(LOGIN_NAME);

        boolean submitted = service.examIsAlreadySubmitted(examFachId, name);

        if (submitted) {
            service.removeOldAnswersAndReviews(examFachId, name);
        }

        boolean success = service.submitExam(name, antworten.getAnswers(), examFachId);

        if (success) {
            redirectAttributes.addFlashAttribute(
                    MESSAGE,
                    "Alle Antworten erfolgreich eingereicht!");
            redirectAttributes.addFlashAttribute(SUCCESS, true);
        } else {
            redirectAttributes.addFlashAttribute(
                    MESSAGE,
                    "Fehler beim Einreichen der Antworten.");
            redirectAttributes.addFlashAttribute(SUCCESS, false);
        }
        return "redirect:/exams/examsStudierende";
    }
}
