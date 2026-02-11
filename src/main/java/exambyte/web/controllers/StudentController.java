package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.web.form.submit_answers.SubmitForm;
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
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/student")
@Secured("ROLE_STUDENT")
public class StudentController {

    private final ExamControllerService service;
    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_EXAM_STUDENT = "redirect:/student/examListForStudent";

    public StudentController(ExamControllerService service) {
        this.service = service;
    }

    private String redirectWithMessage(RedirectAttributes redirectAttributes, String message, boolean success) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("success", success);
        return REDIRECT_EXAM_STUDENT;
    }

    @GetMapping("/examListForStudent")
    public String listExamsForStudents(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute(LOGIN_NAME);

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        double progress = service.getZulassungsProgress(studentName);
        boolean zulassungsStatus = service.hasAnyFailedAttempt(studentName);

        model.addAttribute(TIME_NOW, now);
        model.addAttribute("exams", examDTOs);
        model.addAttribute("name", studentName);
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("progress", progress);
        model.addAttribute("failedYetOrNot", zulassungsStatus);
        return "student/examListForStudent";
    }

    @GetMapping("/startExam/{examFachId}/menu")
    public String examMenu(
            @PathVariable UUID examFachId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String studentLogin = user.getAttribute(LOGIN_NAME);
        ExamDTO examDTO = service.getExamByUUID(examFachId);
        boolean alreadySubmitted = service.examIsAlreadySubmitted(examFachId, studentLogin);

        UUID profFachId = examDTO.professorFachId();
        ProfessorDTO prof = service.getProfessorByFachId(profFachId);

        if (alreadySubmitted) {
            VersuchDTO attempt = service.getAttempt(examFachId, studentLogin);
            model.addAttribute("attempt", attempt);
        }

        boolean reviewPermission = service.checkTimeForReviewView(examFachId);

        ExamTimeInfo examTimeInfo = service.getExamTimeInfo(examDTO);

        model.addAttribute("exam", examDTO);
        model.addAttribute("alreadySubmitted", alreadySubmitted);
        model.addAttribute("timeLeft", examTimeInfo.fristAnzeige());
        model.addAttribute("timeLeftBool", examTimeInfo.timeLeft());
        model.addAttribute("reviewPermission", reviewPermission);
        model.addAttribute("authorName", prof.name());
        return "student/examMenu";
    }

    @GetMapping("/startExam/{examFachId}")
    public String startExam(
            @PathVariable UUID examFachId,
            Model model) {

        ExamForm form = service.fillExamForm(examFachId);

        SubmitForm submitForm = new SubmitForm();

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "student/startExam";
    }

    @PostMapping("/submit/{examFachId}")
    public String submitExam(
            @PathVariable UUID examFachId,
            @Valid @ModelAttribute("submitForm") SubmitForm submitForm,
            BindingResult bindingResult,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Alle Antworten müssen gesetzt werden!",
                    false);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        boolean submitted = service.examIsAlreadySubmitted(examFachId, name);

        if (submitted) {
            service.removeOldAnswersAndReviews(examFachId, name);
        }

        boolean success = service.submitExam(name, submitForm.getAnswers(), examFachId);

        String redirectMsg;

        if (success) redirectMsg = "Alle Antworten erfolgreich eingereicht!";
        else redirectMsg = "Fehler beim Einreichen der Antworten.";
        return redirectWithMessage(redirectAttributes, redirectMsg, success);
    }

    @GetMapping("/startWithData/{examFachId}")
    public String startWithData(
            @PathVariable UUID examFachId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        OldDataForm form = service.fillOldDataForm(examFachId, name);
        SubmitForm submitForm = service.fillSubmitFormWithData(form);

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "student/startExamWithData";
    }

    @GetMapping("/showReview/{examFachId}")
    public String showReview(
            @PathVariable UUID examFachId,
            Model model,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        boolean allowedToShowReview = service.checkTimeForReviewView(examFachId);

        if (!allowedToShowReview) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Korrektureinsicht noch nicht verfügbar!",
                    false);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        ReviewViewForm view = service.prepareReviewViewForm(examFachId, name);

        model.addAttribute("view", view);
        return "student/showReview";
    }
}
