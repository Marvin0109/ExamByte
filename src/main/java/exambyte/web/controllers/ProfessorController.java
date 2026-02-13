package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.show_review.ReviewViewForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/professor")
@Secured("ROLE_ADMIN")
public class ProfessorController {

    private final ExamControllerService service;

    private static final int INT_QUESTIONS_COUNT = 6;
    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_CREATE_EXAM = "redirect:/professor/createExam";
    private static final String REDIRECT_LIST_EXAMS = "redirect:/professor/listExams";

    private final Clock clock;

    public ProfessorController(ExamControllerService service, Clock clock) {
        this.service = service;
        this.clock = clock;
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .truncatedTo(ChronoUnit.MINUTES);
    }

    private String redirectWithMessage(RedirectAttributes redirectAttributes, String message, boolean success,
                                       String redirectedUrl) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("success", success);
        return redirectedUrl;
    }

    @GetMapping("/createExam")
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
        return "professor/createExam";
    }

    @PostMapping("/createExam")
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
                    REDIRECT_CREATE_EXAM);
        }

        if (form.getQuestions().size() < INT_QUESTIONS_COUNT){
            return redirectWithMessage(
                    redirectAttributes,
                    "Weniger Fragen als sonst.",
                    false,
                    REDIRECT_CREATE_EXAM);
        }

        String name = auth.getPrincipal().getAttribute(LOGIN_NAME);
        UUID profFachID = service.getProfFachIDByName(name).orElse(null);

        String message = service.createExam(form, name);

        if (!message.isEmpty()) {
            return redirectWithMessage(
                    redirectAttributes,
                    message,
                    false,
                    REDIRECT_CREATE_EXAM);
        }

        UUID examUUID = service.getExamUUIDByStartTime(form.getStart());

        service.createQuestions(form, profFachID, examUUID);

        return redirectWithMessage(
                redirectAttributes,
                "Prüfung und Fragen erfolgreich erstellt!",
                true,
                REDIRECT_CREATE_EXAM);
    }

    @GetMapping("/listExams")
    public String listExamsForProfessor(
            Model model,
            HttpServletRequest request) {

        List<ExamDTO> examDTOs = service.getAllExams();

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("exams", examDTOs);
        model.addAttribute(TIME_NOW, now());
        return "professor/examListForProf";
    }

    @GetMapping("/listParticipants/{examId}")
    public String listParticipants(
            Model model,
            @PathVariable UUID examId,
            RedirectAttributes redirectAttributes) {

        ExamDTO exam = service.getExamByUUID(examId);

        if (now().isBefore(exam.resultTime())) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Ergebnisse noch nicht vorhanden!",
                    false,
                    REDIRECT_LIST_EXAMS
            );
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", exam);
        model.addAttribute(TIME_NOW, now());
        return "professor/submitStudentList";
    }

    @GetMapping("/showResult/{examId}/{studentName}")
    public String showStudentResult(
            Model model,
            @PathVariable UUID examId,
            @PathVariable String studentName) {

        ReviewViewForm view = service.prepareReviewViewForm(examId, studentName);

        model.addAttribute("view", view);
        return "student/showReview";
    }

    @PostMapping("/deleteExam/{examId}")
    public String deleteExam(
            @PathVariable UUID examId,
            RedirectAttributes redirectAttributes) {

        boolean success = service.deleteExam(examId);

        if (success) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Exam erfolgreich gelöscht!",
                    true,
                    REDIRECT_LIST_EXAMS
            );
        }

        return redirectWithMessage(
                redirectAttributes,
                "Exam am laufen!",
                false,
                REDIRECT_LIST_EXAMS
        );
    }
}
