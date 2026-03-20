package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.AttemptDTO;
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

    @GetMapping("/startExam/{examId}/menu")
    public String examMenu(
            @PathVariable UUID examId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String studentLogin = user.getAttribute(LOGIN_NAME);
        ExamDTO examDTO = service.getExamByUUID(examId);
        boolean alreadySubmitted = service.examIsAlreadySubmitted(examId, studentLogin);

        UUID profId = examDTO.professorId();
        ProfessorDTO prof = service.getProfessorById(profId);

        if (alreadySubmitted) {
            AttemptDTO attempt = service.getAttempt(examId, studentLogin);
            model.addAttribute("attempt", attempt);
        }

        boolean reviewPermission = service.checkTimeForReviewView(examId);

        ExamTimeInfo examTimeInfo = service.getExamTimeInfo(examDTO);

        model.addAttribute("exam", examDTO);
        model.addAttribute("alreadySubmitted", alreadySubmitted);
        model.addAttribute("timeLeft", examTimeInfo.fristAnzeige());
        model.addAttribute("timeLeftBool", examTimeInfo.timeLeft());
        model.addAttribute("reviewPermission", reviewPermission);
        model.addAttribute("authorName", prof.name());
        return "student/examMenu";
    }

    @GetMapping("/startExam/{examId}")
    public String startExam(
            @PathVariable UUID examId,
            Model model) {

        ExamForm form = service.fillExamForm(examId);

        SubmitForm submitForm = new SubmitForm();

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "student/startExam";
    }

    @PostMapping("/submit/{examId}")
    public String submitExam(
            @PathVariable UUID examId,
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

        boolean success = service.submitExam(name, submitForm.getAnswers(), examId);

        String redirectMsg;

        if (success) redirectMsg = "Alle Antworten erfolgreich eingereicht!";
        else redirectMsg = "Fehler beim Einreichen der Antworten.";
        return redirectWithMessage(redirectAttributes, redirectMsg, success);
    }

    @GetMapping("/startWithData/{examId}")
    public String startWithData(
            @PathVariable UUID examId,
            Model model,
            OAuth2AuthenticationToken auth) {

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        OldDataForm form = service.fillOldDataForm(examId, name);
        SubmitForm submitForm = service.fillSubmitFormWithData(form);

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "student/startExamWithData";
    }

    @GetMapping("/showReview/{examId}")
    public String showReview(
            @PathVariable UUID examId,
            Model model,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        boolean allowedToShowReview = service.checkTimeForReviewView(examId);

        if (!allowedToShowReview) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Korrektureinsicht noch nicht verfügbar!",
                    false);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        ReviewViewForm view = service.prepareReviewViewForm(examId, name);

        model.addAttribute("view", view);
        return "student/showReview";
    }
}
