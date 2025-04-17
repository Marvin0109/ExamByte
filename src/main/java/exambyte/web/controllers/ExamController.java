package exambyte.web.controllers;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.web.form.ExamData;
import exambyte.web.form.ExamForm;
import exambyte.web.form.QuestionData;
import exambyte.web.form.helper.QuestionType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/exams")
public class ExamController {

    private final ExamManagementService examManagementService;

    @Autowired
    public ExamController(ExamManagementService examManagementService) {
       this.examManagementService = examManagementService;
    }

    @GetMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String showCreateExamForm(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        model.addAttribute("examForm", new ExamForm());
        return "/exams/examsProfessoren";
    }

    @PostMapping("/examsProfessoren")
    @Secured("ROLE_ADMIN")
    public String createExam(
            @Valid @ModelAttribute ExamForm form,
            @RequestBody ExamData examData, // Empfang der Exam-Daten aus JSON (da sind nur Fragedaten drin)
            Model model,
            OAuth2AuthenticationToken auth,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("message",
                    "Fehlerhafte Eingabe: " + bindingResult.getAllErrors().toString());
            return "/exams/examsProfessoren";
        }

        String name = auth.getPrincipal().getAttribute("login");

        boolean success = examManagementService.createExam(name, form.getTitle(), form.getStart(),
                form.getEnd(), form.getResult());

        UUID profFachID =
        examManagementService
            .getProfFachIDByName(name)
            .orElseThrow(NichtVorhandenException::new);

        UUID examUUID =
            examManagementService.getExamByStartTime(form.getStart());

        if (success) {
            for(QuestionData question : examData.getQuestions()) {
                String frageText = question.getQuestionText();
                QuestionType frageTyp = QuestionType.valueOf(question.getType());
                int maxPunkte = question.getPunkte();

                if (frageTyp.equals(QuestionType.FREITEXT)) {
                    examManagementService.createFrage(new FrageDTO(null, null, frageText,
                            maxPunkte, profFachID, examUUID));
                } else if (frageTyp.equals(QuestionType.SINGLE_CHOICE)) {
                    String correctAnswer = question.getCorrectAnswer();
                    String frageTextWithChoices = frageText + "\n || \n" + String.join(" ", question.getChoices());
                    FrageDTO f = new FrageDTO(null, null, frageTextWithChoices, maxPunkte, profFachID, examUUID);
                    examManagementService.createChoiceFrage(f, new KorrekteAntwortenDTO(null, null,
                            f.getFachId(), correctAnswer));
                } else if (frageTyp.equals(QuestionType.MULTIPLE_CHOICE)) {
                    List<String> correctAnswers = question.getCorrectAnswers();
                    String frageTextWithChoices = frageText + "\n || \n" + String.join(" ", question.getChoices());
                    FrageDTO f = new FrageDTO(null, null, frageTextWithChoices, maxPunkte, profFachID, examUUID);
                    examManagementService.createChoiceFrage(f, new KorrekteAntwortenDTO(null, null, f.getFachId(),
                            String.join(",", correctAnswers)));
                }
            }

            redirectAttributes.addFlashAttribute("message","Test erfolgreich erstellt!");
            redirectAttributes.addFlashAttribute("exam", new ExamDTO(
                    null, null, form.getTitle(), profFachID, form.getStart(), form.getEnd(), form.getResult()
            ));

        } else {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Erstellen der Prüfung.");
        }
        return "redirect:/exams/examsProfessoren";
    }

    @GetMapping("/examsKorrektor")
    @Secured("ROLE_REVIEWER")
    public String listExamsForReviewer(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsKorrektor";
    }

    @GetMapping("/examStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsStudierende";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(@PathVariable UUID examFachId, OAuth2AuthenticationToken auth, Model model) {
        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute("login");
        boolean alreadySubmitted = examManagementService.isExamAlreadySubmitted(examFachId, user.getAttribute("login"));
        ExamDTO examDTO = examManagementService.getExam(examFachId);
        List<FrageDTO> fragen = examManagementService.getFragenForExam(examFachId);


        model.addAttribute("exam", examDTO);
        model.addAttribute("fragen", fragen);
        model.addAttribute("alreadySubmitted", alreadySubmitted); // Gibt die True oder False ans Formular, false wenn Anzahl an Exams (12) erreicht ist
        model.addAttribute("name", studentName);
        return "/exams/examsDurchfuehren";
    }

    @PostMapping("/submit")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @RequestParam List<UUID> frageFachIds,
            @RequestParam List<String> antwortTexte,
            OAuth2AuthenticationToken auth,
            Model model) {

        OAuth2User user = auth.getPrincipal();

        boolean success =
        examManagementService.submitExam(user.getAttribute("login"), frageFachIds, antwortTexte);

        if (success) {
            model.addAttribute("message", "Alle Antworten erfolgreich eingereicht!");
        } else {
            model.addAttribute("message", "Fehler beim Einreichen der Antworten.");
        }
        return "redirect:/exams/list";
    }

    // TODO: unvollständig, Fragetypen fehlen (MC, SC und Freitext)
    @GetMapping("/showExam/{id}")
    @Secured("ROLE_REVIEWER")
    public String showExam(Model model, @PathVariable UUID id) {
        ExamDTO exam = examManagementService.getExam(id);
        List<FrageDTO> fragen = examManagementService.getFragenForExam(id);
        model.addAttribute("exam", exam);
        model.addAttribute("frage", fragen);
        return "/exams/exam";
    }
}
