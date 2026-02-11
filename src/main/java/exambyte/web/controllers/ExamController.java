package exambyte.web.controllers;

import exambyte.application.dto.*;
import exambyte.web.form.load_old_submit_data.OldDataForm;
import exambyte.web.form.show_review.ReviewViewForm;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.create_review.ReviewForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.submit_answers.SubmitForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamControllerService service;
    private static final int INT_QUESTIONS_COUNT = 6;
    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String MESSAGE = "message";
    private static final String SUCCESS = "success";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_EXAM_PROF = "redirect:/exams/createExam";
    private static final String REDIRECT_EXAM_KORREKTOR = "redirect:/exams/examListForReviewer";
    private static final String REDIRECT_EXAM_STUDENT = "redirect:/exams/examListForStudent";

    public  ExamController(ExamControllerService service) {
        this.service =  service;
    }

    @GetMapping("/createExam")
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
        return "exams/createExam";
    }

    @PostMapping("/createExam")
    @Secured("ROLE_ADMIN")
    public String createExam(
            @Valid ExamForm form,
            BindingResult bindingResult,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Fehlerhafte Eingabedaten!",
                    false,
                    REDIRECT_EXAM_PROF);
        }

        if (form.getQuestions().size() < INT_QUESTIONS_COUNT){
            return redirectWithMessage(
                    redirectAttributes,
                    "Weniger Fragen als sonst.",
                    false,
                    REDIRECT_EXAM_PROF);
        }

        String name = auth.getPrincipal().getAttribute(LOGIN_NAME);
        UUID profFachID = service.getProfFachIDByName(name).orElse(null);

        String message = service.createExam(form, name);

        if (!message.isEmpty()) {
            return redirectWithMessage(
                    redirectAttributes,
                    message,
                    false,
                    REDIRECT_EXAM_PROF);
        }

        UUID examUUID = service.getExamUUIDByStartTime(form.getStart());

        service.createQuestions(form, profFachID, examUUID);

        return redirectWithMessage(
                redirectAttributes,
                "Prüfung und Fragen erfolgreich erstellt!",
                true,
                REDIRECT_EXAM_PROF);
    }

    @GetMapping("/showResults")
    @Secured("ROLE_ADMIN")
    public String listExamsForProfessor(
            Model model,
            HttpServletRequest request) {

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("exams", examDTOs);
        model.addAttribute(TIME_NOW, now);
        return "exams/examListForProf";
    }


    @GetMapping("/examListForReviewer")
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
        return "exams/examListForReviewer";
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
            return redirectWithMessage(
                    redirectAttributes,
                    "Die Prüfung läuft noch! Keine Korrektur erlaubt.",
                    false,
                    REDIRECT_EXAM_KORREKTOR);
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examFachId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", examDTO);
        model.addAttribute(TIME_NOW, now);
        return "exams/examSubmitsView";
    }

    @GetMapping("/showSubmit/{examFachId}/{studentFachId}")
    @Secured("ROLE_REVIEWER")
    public String showSubmit(
            Model model,
            @PathVariable UUID examFachId,
            @PathVariable UUID studentFachId) {

        Map<FrageDTO, AntwortDTO> frageAntwortMap =
                service.getFreitextAntwortenForExamAndStudent(examFachId, studentFachId);

        List<AnswerForm> antwortForm = service.createAnswerForm(frageAntwortMap);
        ReviewForm reviewForm = new ReviewForm();

        model.addAttribute("antworten", antwortForm);
        model.addAttribute("reviewForm", reviewForm);
        return "exams/showSubmit";
    }

    @PostMapping("/createReview/{antwortFachId}")
    @Secured("ROLE_REVIEWER")
    public String createReview(
            @Valid ReviewForm reviewForm,
            BindingResult bindingResult,
            @PathVariable UUID antwortFachId,
            RedirectAttributes redirectAttributes,
            OAuth2AuthenticationToken auth) {

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                            .findFirst()
                            .map(FieldError::getDefaultMessage)
                            .orElse("Ungültige Eingabe");
            return redirectWithMessage(redirectAttributes, message, false, REDIRECT_EXAM_KORREKTOR);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);
        UUID korrektorFachId = service.getReviewerByName(name);
        service.createReview(reviewForm, antwortFachId, korrektorFachId);

        return redirectWithMessage(
                redirectAttributes,
                "Bewertung erfolgreich!",
                true,
                REDIRECT_EXAM_KORREKTOR);
    }

    @GetMapping("/examListForStudent")
    @Secured("ROLE_STUDENT")
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
        return "exams/examListForStudent";
    }

    @GetMapping("/startExam/{examFachId}/menu")
    @Secured("ROLE_STUDENT")
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
        return "exams/examMenu";
    }

    @GetMapping("/startExam/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(
            @PathVariable UUID examFachId,
            Model model) {

        ExamForm form = service.fillExamForm(examFachId);

        SubmitForm submitForm = new SubmitForm();

        model.addAttribute("exam", form);
        model.addAttribute("submitForm", submitForm);
        return "exams/startExam";
    }

    @PostMapping("/submit/{examFachId}")
    @Secured("ROLE_STUDENT")
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
                    false,
                    REDIRECT_EXAM_STUDENT);
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
        return redirectWithMessage(redirectAttributes, redirectMsg, success, REDIRECT_EXAM_STUDENT);
    }

    @GetMapping("/startWithData/{examFachId}")
    @Secured("ROLE_STUDENT")
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
        return "exams/startExamWithData";
    }

    @GetMapping("/showReview/{examFachId}")
    @Secured("ROLE_STUDENT")
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
                    false,
                    REDIRECT_EXAM_STUDENT);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        ReviewViewForm view = service.prepareReviewViewForm(examFachId, name);

        model.addAttribute("view", view);
        return "exams/showReview";
    }

    private String redirectWithMessage(RedirectAttributes redirectAttributes, String message, boolean success,
                                       String redirectedUrl) {
        redirectAttributes.addFlashAttribute(MESSAGE, message);
        redirectAttributes.addFlashAttribute(SUCCESS, success);
        return redirectedUrl;
    }
}
