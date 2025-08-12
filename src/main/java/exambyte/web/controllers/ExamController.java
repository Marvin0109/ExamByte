package exambyte.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.web.form.ExamForm;
import exambyte.web.form.QuestionData;
import exambyte.web.form.helper.QuestionType;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.unbescape.csv.CsvEscape.escapeCsv;

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

        ExamForm examForm = new ExamForm();

        for (int i = 0; i < 6; i++) {
            QuestionData q = new QuestionData();
            q.setIndex(i);
            q.setQuestionText("");
            q.setType("");
            q.setPunkte(0);
            q.setChoices(new ArrayList<>());
            q.setCorrectAnswers(new ArrayList<>());
            examForm.getQuestions().add(q);
        }

        model.addAttribute("name", user.getAttribute("login"));
        model.addAttribute("examForm", examForm);

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
            return "/exams/examsProfessoren";
        }

        String name = auth.getPrincipal().getAttribute("login");

        boolean success = examManagementService.createExam(
                name,
                form.getTitle(),
                form.getStart(),
                form.getEnd(),
                form.getResult()
        );

        if (!success) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Erstellen der Prüfung.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/exams/examsProfessoren";
        }

        UUID profFachID = examManagementService.getProfFachIDByName(name)
                .orElseThrow(NichtVorhandenException::new);

        UUID examUUID = examManagementService.getExamByStartTime(form.getStart());

        for (QuestionData q : form.getQuestions()) {
            String frageText = q.getQuestionText();
            QuestionType frageTyp = QuestionType.valueOf(q.getType());
            int maxPunkte = q.getPunkte();

            if (frageTyp.equals(QuestionType.FREITEXT)) {
                examManagementService.createFrage(new FrageDTO(null, null, frageText,
                        maxPunkte, profFachID, examUUID));
            } else if (frageTyp.equals(QuestionType.SINGLE_CHOICE)) {
                String correctAnswer = q.getCorrectAnswer();
                FrageDTO f = new FrageDTO(null, null, frageText, maxPunkte, profFachID, examUUID);
                examManagementService.createChoiceFrage(f, new KorrekteAntwortenDTO(null, null,
                        f.getFachId(), correctAnswer));
            } else if (frageTyp.equals(QuestionType.MULTIPLE_CHOICE)) {
                List<String> correctAnswers = q.getCorrectAnswers();
                String correctAnswersAsString = correctAnswers.getFirst();
                FrageDTO f = new FrageDTO(null, null, frageText, maxPunkte, profFachID, examUUID);
                examManagementService.createChoiceFrage(f, new KorrekteAntwortenDTO(null, null, f.getFachId(),
                        correctAnswersAsString));
            }
        }

        redirectAttributes.addFlashAttribute("message", "Prüfung und Fragen erfolgreich erstellt!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/exams/examsProfessoren";
    }

    @PostMapping("/examsProfessoren/success")
    @Secured("ROLE_ADMIN")
    public String testSuccessMessage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "It's working!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/exams/examsProfessoren";
    }

    @PostMapping("/examsProfessoren/reset")
    @Secured("ROLE_ADMIN")
    public String resetExamData() {
        examManagementService.reset();
        return "redirect:/exams/examsProfessoren";
    }

    @PostMapping("/examsProfessoren/export")
    @Secured("ROLE_ADMIN")
    public String exportExamFormCsv(
            @Valid ExamForm form,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("examForm", form);
            return "exams/examsProfessoren";
        }

        // CSV Response vorbereiten
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"exam_form_export.csv\"");

        PrintWriter writer = response.getWriter();

        // Beispiel Header (z.B. für Fragen)
        writer.println("FrageIndex,Fragetitel,Fragetext,Typ,Punkte,Korrekte Antwort(en),Antwortmöglichkeiten");

        for (QuestionData q : form.getQuestions()) {
            String choices = "";
            if (q.getChoices() != null) {
                // Optionen als Zeilenumbruch-getrennte Liste oder Komma-getrennt
                choices = String.join(" | ", q.getChoices());
            }

            String correctAnswers = "";
            if (q.getCorrectAnswers() != null && !q.getCorrectAnswers().isEmpty()) {
                correctAnswers = String.join(" | ", q.getCorrectAnswers());
            } else if (q.getCorrectAnswer() != null) {
                correctAnswers = q.getCorrectAnswer();
            }

            // CSV-Eintrag (escapeCsv solltest du noch implementieren)
            writer.printf("%d,%s,%s,%s,%d,%s,%s%n",
                    q.getIndex(),
                    escapeCsv(q.getTitle()),
                    escapeCsv(q.getQuestionText()),
                    q.getType(),
                    q.getPunkte(),
                    escapeCsv(correctAnswers),
                    escapeCsv(choices)
            );
        }

        writer.flush();

        return null;
    }

    // TODO: HTML ausbauen für Exam Liste
    @GetMapping("/examsKorrektor")
    @Secured("ROLE_REVIEWER")
    public String listExamsForReviewer(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsKorrektor";
    }

    // TODO: HTML ausbauen für Exam Liste
    @GetMapping("/examStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        return "/exams/examsStudierende";
    }

    // TODO: Durchführen von Exams implementieren in HTML
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
