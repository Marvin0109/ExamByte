package exambyte.web.controllers;

import exambyte.application.dto.AnswerDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.QuestionDTO;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.create_review.AnswerForm;
import exambyte.web.form.create_review.ReviewForm;
import exambyte.web.form.info.ReviewCoverageForm;
import exambyte.web.form.info.SubmitInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/reviewer")
@Secured("ROLE_REVIEWER")
public class ReviewerController {

    private final ExamControllerService service;
    private static final String LOGIN_NAME = "login";
    private static final String CURRENT_PATH = "currentPath";
    private static final String TIME_NOW = "timeNow";
    private static final String REDIRECT_EXAM_REVIEWER = "redirect:/reviewer/examListForReviewer";

    public ReviewerController(ExamControllerService service) {
        this.service = service;
    }

    private String redirectWithMessage(RedirectAttributes redirectAttributes, String message, boolean success) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("success", success);
        return REDIRECT_EXAM_REVIEWER;
    }

    @GetMapping("/examListForReviewer")
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
        return "reviewer/examListForReviewer";
    }

    @GetMapping("/showExamSubmits/{examId}")
    public String showExamSubmits(
            Model model,
            @PathVariable UUID examId,
            RedirectAttributes redirectAttributes) {

        ExamDTO examDTO = service.getExamByUUID(examId);
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(examDTO.end())) {
            return redirectWithMessage(
                    redirectAttributes,
                    "Die Prüfung läuft noch! Keine Korrektur erlaubt.",
                    false);
        }

        List<SubmitInfo> submitInfoList = service.getSubmitInfo(examId);

        model.addAttribute("submitInfoList", submitInfoList);
        model.addAttribute("exam", examDTO);
        model.addAttribute(TIME_NOW, now);
        return "reviewer/examSubmitsView";
    }

    @GetMapping("/showSubmit/{examId}/{studentId}")
    public String showSubmit(
            Model model,
            @PathVariable UUID examId,
            @PathVariable UUID studentId) {

        Map<QuestionDTO, AnswerDTO> questionAnswerMap =
                service.getFreeResponseSolutionForExamAndStudent(examId, studentId);

        List<AnswerForm> answerForm = service.createAnswerForm(questionAnswerMap);
        ReviewForm reviewForm = new ReviewForm();

        model.addAttribute("answers", answerForm);
        model.addAttribute("reviewForm", reviewForm);
        return "reviewer/showSubmit";
    }

    @PostMapping("/createReview/{answerId}")
    public String createReview(
            @Valid ReviewForm reviewForm,
            BindingResult bindingResult,
            @PathVariable UUID answerId,
            RedirectAttributes redirectAttributes,
            OAuth2AuthenticationToken auth) {

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse("Ungültige Eingabe");
            return redirectWithMessage(redirectAttributes, message, false);
        }

        OAuth2User user = auth.getPrincipal();
        String name = user.getAttribute(LOGIN_NAME);
        UUID reviewerId = service.getReviewerByName(name);
        service.createReview(reviewForm, answerId, reviewerId);

        return redirectWithMessage(
                redirectAttributes,
                "Bewertung erfolgreich!",
                true);
    }
}
