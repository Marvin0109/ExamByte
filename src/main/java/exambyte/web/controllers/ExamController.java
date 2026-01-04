package exambyte.web.controllers;

import exambyte.application.common.QuestionTypeDTO;
import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.FrageDTO;
import exambyte.application.dto.KorrekteAntwortenDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.NichtVorhandenException;
import exambyte.web.common.QuestionTypeWeb;
import exambyte.web.form.ExamForm;
import exambyte.web.form.QuestionData;
import exambyte.web.form.SubmitForm;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public String showCreateExamForm(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {
        OAuth2User user = auth.getPrincipal();

        ExamForm examForm = new ExamForm();

        for (int i = 0; i < 6; i++) {
            QuestionData q = new QuestionData();
            q.setIndex(i);
            q.setQuestionText("");
            q.setType("");
            q.setPunkte(0);
            q.setChoices("");
            q.setCorrectAnswers("");
            q.setCorrectAnswer("");
            examForm.getQuestions().add(q);
        }

        model.addAttribute("name", user.getAttribute("login"));
        model.addAttribute("examForm", examForm);
        model.addAttribute("currentPath", request.getRequestURI());
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
            redirectAttributes.addFlashAttribute("message", "Fehler beim Erstellen der Fragen.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Fehler: " + error.getDefaultMessage());
            });

            return "redirect:/exams/examsProfessoren";
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

        System.out.println(form.getQuestions().size());

        if (form.getQuestions().size() < 6){
            redirectAttributes.addFlashAttribute("message", "Weniger Fragen als sonst.");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/exams/examsProfessoren";
        }

        for (QuestionData q : form.getQuestions()) {
            String frageText = q.getQuestionText();
            QuestionTypeWeb frageTyp = QuestionTypeWeb.valueOf(q.getType().trim());
            int maxPunkte = q.getPunkte();

            switch(frageTyp) {
                case QuestionTypeWeb.FREITEXT:
                    examManagementService.createFrage(new FrageDTO(null, null, frageText,
                            maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()), profFachID, examUUID));
                    break;
                case QuestionTypeWeb.SC:
                    String correctAnswer = q.getCorrectAnswer();
                    FrageDTO f1 = new FrageDTO(null, null, frageText, maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()),
                            profFachID, examUUID);
                    examManagementService.createChoiceFrage(f1, new KorrekteAntwortenDTO(null, null,
                            f1.getFachId(), correctAnswer, q.getChoices()));
                    break;
                case QuestionTypeWeb.MC:
                    String correctAnswers = q.getCorrectAnswers();
                    FrageDTO f2 = new FrageDTO(null, null, frageText, maxPunkte, QuestionTypeDTO.valueOf(frageTyp.name()),
                            profFachID, examUUID);
                    examManagementService.createChoiceFrage(f2, new KorrekteAntwortenDTO(null, null, f2.getFachId(),
                            correctAnswers, q.getChoices()));
                    break;
                default:
            }
        }

        redirectAttributes.addFlashAttribute("message", "Prüfung und Fragen erfolgreich erstellt!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/exams/examsProfessoren";
    }

    // TODO: Braucht überarbeitung
    // TODO: CSV-Export nur für Testergebnisse
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
            writer.printf("%s,%s,%s,%s,%s,%s%n",
                    q.getIndex(),
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
    public String listExamsForReviewer(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        model.addAttribute("currentPath", request.getRequestURI());
        return "/exams/examsKorrektor";
    }

    @GetMapping("/examsStudierende")
    @Secured("ROLE_STUDENT")
    public String listExamsForStudents(
            Model model,
            OAuth2AuthenticationToken auth,
            HttpServletRequest request) {
        OAuth2User user = auth.getPrincipal();
        model.addAttribute("name", user.getAttribute("login"));
        List<ExamDTO> examDTOs = examManagementService.getAllExams();
        model.addAttribute("exams", examDTOs);
        model.addAttribute("currentPath", request.getRequestURI());
        return "/exams/examsStudierende";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}/menu")
    @Secured("ROLE_STUDENT")
    public String examMenu(@PathVariable("examFachId") String examFachId,
                           Model model,
                           OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        String studentLogin = user.getAttribute("login");
        ExamDTO examDTO = examManagementService.getExam(UUID.fromString(examFachId));
        boolean alreadySubmitted = examManagementService.isExamAlreadySubmitted(
                UUID.fromString(examFachId),
                studentLogin
        );

        UUID author = examDTO.professorFachId();
        String authorIDString = author.toString();

        if (alreadySubmitted) {
            List<VersuchDTO> allAttempts = examManagementService.getSubmission(
                    UUID.fromString(examFachId), studentLogin
            );
            model.addAttribute("attempts", allAttempts);
        } else {
            model.addAttribute("attempts", Collections.emptyList());
        }

        String fristAnzeige = "";
        String tageAnzeige = "";
        String stundenAnzeige = "";
        String minutenAnzeige = "";
        boolean timeLeft = false;

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(examDTO.endTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy, HH:mm");
            String endTimeFormatted = examDTO.endTime().format(formatter);

            fristAnzeige = "Sie haben die längstmögliche Bearbeitungsdauer des Tests überschritten. Der Test " +
                    "konnte nur bis " + endTimeFormatted + " bearbeitet werden.";
        } else {
            Duration diff = Duration.between(LocalDateTime.now(), examDTO.endTime());

            long days = diff.toDays();
            long hours = diff.toHours() % 24;
            long minutes = diff.toMinutes() % 60;

            if (days == 1) {
                tageAnzeige = days + " Tag";
            } else if (days > 1) {
                tageAnzeige = days + " Tage";
            }

            if (hours == 1) {
                stundenAnzeige = hours + " Stunde";
            } else if (hours > 1) {
                stundenAnzeige = hours + " Stunden";
            }

            if (minutes == 1) {
                minutenAnzeige = minutes + " Minute";
            } else if (minutes > 1) {
                minutenAnzeige = minutes + " Minuten";
            }

            if (!tageAnzeige.isEmpty()) {
                fristAnzeige += tageAnzeige + " ";
            }
            if (!stundenAnzeige.isEmpty()) {
                fristAnzeige += stundenAnzeige + " ";
            }
            if (!minutenAnzeige.isEmpty()) {
                fristAnzeige += minutenAnzeige;
            }

            timeLeft = true;
        }

        model.addAttribute("exam", examDTO);
        model.addAttribute("alreadySubmitted", alreadySubmitted);
        model.addAttribute("timeLeft", fristAnzeige);
        model.addAttribute("timeLeftBool", timeLeft);
        model.addAttribute("checkboxChecked", false);
        model.addAttribute("authorID", authorIDString);
        return "/exams/examMenu";
    }

    @GetMapping("/examsDurchfuehren/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String startExam(@PathVariable("examFachId") String examFachId, OAuth2AuthenticationToken auth, Model model) {
        OAuth2User user = auth.getPrincipal();
        String studentName = user.getAttribute("login");

        ExamDTO examDTO = examManagementService.getExam(UUID.fromString(examFachId));
        List<FrageDTO> fragen = examManagementService.getFragenForExam(UUID.fromString(examFachId));

        ExamForm form = new ExamForm();
        form.setStart(examDTO.startTime());
        form.setEnd(examDTO.endTime());
        form.setEnd(examDTO.endTime());
        form.setTitle(examDTO.title());
        form.setFachId(UUID.fromString(examFachId));

        // TODO: Index wird nicht erhöht?
        int index = 0;

        List<QuestionData> questions = new ArrayList<>();

        for (FrageDTO frage : fragen) {
            QuestionData questionData = new QuestionData();
            questionData.setIndex(index);
            questionData.setQuestionText(frage.getFrageText());
            questionData.setPunkte(frage.getMaxPunkte());
            questionData.setType(frage.getType().toString());
            questionData.setFachId(frage.getFachId());
            if (questionData.getType().equals("MC") || questionData.getType().equals("SC")) {
                String choice = examManagementService.getChoiceForFrage(frage.getFachId());
                questionData.setChoices(choice.replace("\n", ","));
                System.out.println(questionData.getChoices());
            }
            questions.add(questionData);
        }

        form.setQuestions(questions);

        SubmitForm submitForm = new SubmitForm();

        model.addAttribute("exam", form);
        model.addAttribute("name", studentName);
        model.addAttribute("submitForm", submitForm);
        return "/exams/examsDurchfuehren";
    }

    @PostMapping("/submit/{examFachId}")
    @Secured("ROLE_STUDENT")
    public String submitExam(
            @PathVariable("examFachId") String examFachId,
            @ModelAttribute SubmitForm antworten,
            OAuth2AuthenticationToken auth,
            RedirectAttributes redirectAttributes) {

        OAuth2User user = auth.getPrincipal();

        System.out.println("Antworten List: " + antworten.getAnswers().toString());

        boolean submitted = examManagementService.isExamAlreadySubmitted(UUID.fromString(examFachId),
                user.getAttribute("login"));

        if (submitted) {
            examManagementService.removeOldAnswers(UUID.fromString(examFachId), user.getAttribute("login"));
            System.out.println("Deleting old answers and reviews");
        }

        boolean success =
        examManagementService.submitExam(user.getAttribute("login"), antworten.getAnswers(), UUID.fromString(examFachId));

        if (success) {
            redirectAttributes.addFlashAttribute("message",
                    "Alle Antworten erfolgreich eingereicht!");
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Fehler beim Einreichen der Antworten.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/exams/examsStudierende";
    }

    // TOOD: Das korrigieren der Freitextantworten ausbauen
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
