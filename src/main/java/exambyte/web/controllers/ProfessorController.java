package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.application.service.CsvExportService;
import exambyte.application.service.ExamControllerService;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.create_exam.QuestionSettings;
import exambyte.web.form.info.SubmitInfo;
import exambyte.web.form.show_exam.ExamViewForm;
import exambyte.web.form.show_review.ReviewViewForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/professor")
@SessionAttributes("questionForm")
@Secured("ROLE_ADMIN")
public class ProfessorController {

    private final ExamControllerService service;
    private final CsvExportService exportService;

    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_QUESTION_SETTINGS = "redirect:/professor/questionSettings";
    private static final String REDIRECT_LIST_EXAMS = "redirect:/professor/listExams";
    private static final String QUESTION_SETTINGS = "professor/questionSettings";

    private final Clock clock;

    public ProfessorController(ExamControllerService service, CsvExportService exportService, Clock clock) {
        this.service = service;
        this.exportService = exportService;
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

    @ModelAttribute("questionForm")
    public QuestionSettings createQuestionSettingsForm() {
        return new QuestionSettings();
    }

    @GetMapping("/questionSettings")
    public String questionSettings(
            Model model,
            HttpServletRequest request) {

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return QUESTION_SETTINGS;
    }

    @PostMapping("/generateQuestions")
    public String generateQuestions(
            @Valid @ModelAttribute("questionForm") QuestionSettings questionSettings,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return QUESTION_SETTINGS;
        }

        List<QuestionTypeWeb> typeList = service.createQuestionTypeList(
                questionSettings.getMcCount(),
                questionSettings.getScCount(),
                questionSettings.getFreeResponseCount()
        );

        questionSettings.setQuestionTypeList(typeList);

        return "redirect:/professor/createExam";
    }

    @GetMapping("/createExam")
    public String showCreateExamForm(
            @Valid @ModelAttribute("questionForm") QuestionSettings questionSettings,
            BindingResult bindingResult,
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return QUESTION_SETTINGS;
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);

        int sum = questionSettings.getMcCount()
                + questionSettings.getScCount()
                + questionSettings.getFreeResponseCount();

        ExamForm examForm = service.createExamForm(sum);

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
            RedirectAttributes redirectAttributes,
            SessionStatus status) {

        if (bindingResult.hasErrors()) {
            return "professor/createExam";
        }

        String name = auth.getPrincipal().getAttribute(LOGIN_NAME);

        String message = service.createExam(form, name);

        if (!message.isEmpty()) {
            return redirectWithMessage(
                    redirectAttributes,
                    message,
                    false,
                    REDIRECT_QUESTION_SETTINGS);
        }

        UUID examId = service.getExamUUIDByStartTime(form.getStart());

        service.createQuestions(form, examId);

        status.setComplete();

        return redirectWithMessage(
                redirectAttributes,
                "Prüfung und Fragen erfolgreich erstellt!",
                true,
                REDIRECT_QUESTION_SETTINGS);
    }

    @GetMapping("/listExams")
    public String listExamsForProfessor(
            Model model,
            HttpServletRequest request) {

        List<ExamDTO> exams = service.getAllExams();

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("exams", exams);
        model.addAttribute(TIME_NOW, now());
        return "professor/examListForProf";
    }

    @GetMapping("/showExam/{examId}")
    public String showExamForm(
            @PathVariable UUID examId,
            Model model) {

        ExamViewForm exam = service.getExamView(examId);

        model.addAttribute("exam", exam);
        return "professor/examView";
    }

    @GetMapping("/listParticipants/{examId}")
    public String listParticipants(
            Model model,
            @PathVariable UUID examId,
            RedirectAttributes redirectAttributes) {

        ExamDTO exam = service.getExamByUUID(examId);

        if (now().isBefore(exam.result())) {
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

        return redirectWithMessage(
                redirectAttributes,
                success ? "Prüfung erfolgreich gelöscht!" : "Prüfung am laufen!",
                success,
                REDIRECT_LIST_EXAMS
        );
    }

    @GetMapping("/downloadExam/{examId}")
    public ResponseEntity<byte[]> downloadExam(@PathVariable UUID examId) {
        try {
            List<ExamExportDTO> examDTOs = service.getExamExport(examId);
            ExamDTO exam = service.getExamByUUID(examId);

            byte[] csvBytes = exportService.exportExamToCsv(examDTOs);

            String fileName = exam.title() + ".csv";

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/downloadReview/{examId}/{studentName}")
    public ResponseEntity<byte[]> downloadReview(@PathVariable UUID examId,
                                                 @PathVariable String studentName) {
        try {
            List<ReviewExportDTO> reviewDTOs = service.getReviewExport(examId, studentName);
            ExamDTO exam = service.getExamByUUID(examId);

            byte[] csvBytes = exportService.exportReviewToCsv(reviewDTOs);

            String fileName = exam.title() + "_" + studentName + ".csv";

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
