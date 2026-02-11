package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.SubmitInfo;
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

import java.time.LocalDateTime;
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
    private static final String REDIRECT_EXAM_PROF = "redirect:/professor/createExam";

    public ProfessorController(ExamControllerService service) {
        this.service = service;
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
    public String listExamsForProfessor(
            Model model,
            HttpServletRequest request) {

        List<ExamDTO> examDTOs = service.getAllExams();
        LocalDateTime now = LocalDateTime.now();

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("exams", examDTOs);
        model.addAttribute(TIME_NOW, now);
        return "professor/examListForProf";
    }

    @GetMapping("/listParticipants/{examId}")
    public String listParticipants(
            Model model,
            @PathVariable UUID examId,
            RedirectAttributes redirectAttributes) {

        ExamDTO exam = service.getExamByUUID(examId);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(exam.resultTime())) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Ergebnisse noch nicht vorhanden!",
                    false,
                    "redirect:/professor/showResults"
            );
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", exam);
        model.addAttribute(TIME_NOW, now);
        return "professor/submitStudentList";
    }
}
