package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.csv_dto.ExamExportDTO;
import exambyte.application.dto.csv_dto.ReviewExportDTO;
import exambyte.application.service.AppUserService;
import exambyte.application.service.CsvExportService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.application.service.ExamControllerService;
import exambyte.web.form.show_exam.ExamViewForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class, ProfessorControllerTest.ClockConfig.class})
class ProfessorControllerTest {

    @TestConfiguration
    static class ClockConfig {
        @Bean
        public Clock clock() {
            return Clock.fixed(
                    Instant.parse("2026-01-01T10:00:00Z"),
                    ZoneId.of("UTC")
            );
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @MockitoBean
    private CsvExportService csvExportService;

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Seite für die Eingabe von Anzahl der Fragetypen ist erreichbar mit entsprechender Rolle")
    void questionSettings() throws Exception {
        mvc.perform(get("/professor/questionSettings"))
            .andExpect(status().isOk())
            .andExpect(view().name("professor/questionSettings"))
            .andExpect(model().attributeExists("questionForm"))
            .andExpect(model().attributeExists("currentPath"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Valide Eingabedaten für Generierung von Prüfungsformular")
    void generateQuestions_01() throws Exception {
        MvcResult result = mvc.perform(post("/professor/generateQuestions")
                    .with(csrf())
                .param("mcCount", "1")
                .param("scCount", "4")
                .param("freitextCount", "5"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/createExam"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);
        assertNotNull(session.getAttribute("questionForm"));

        verify(service).createQuestionTypeList(1, 4, 5);
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Invalide Eingabedaten für die Generierung von Prüfungsformular (0 MC Fragen)")
    void generateQuestions_02() throws Exception {
        mvc.perform(post("/professor/generateQuestions")
                    .with(csrf())
                .param("mcCount", "0")
                .param("scCount", "1")
                .param("freitextCount", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Invalide Eingabedaten!"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Invalide Eingabedaten für die Generierung von Prüfungsformular (11 Freitext Fragen)")
    void generateQuestions_03() throws Exception {
        mvc.perform(post("/professor/generateQuestions")
                    .with(csrf())
                .param("mcCount", "1")
                .param("scCount", "1")
                .param("freitextCount", "11"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Invalide Eingabedaten!"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Invalide Eingabedaten für die Generierung von Prüfungsformular (SC Fragen fehlen)")
    void generateQuestions_04() throws Exception {
        mvc.perform(post("/professor/generateQuestions")
                    .with(csrf())
                .param("mcCount", "1")
                .param("freitextCount", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Invalide Eingabedaten!"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void showCreateExamForm_01() throws Exception {

        MvcResult mvcResult = mvc.perform(get("/professor/createExam"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für Professoren sichtbar")
    void showCreateExamForm_02() throws Exception {

        ExamForm form = new ExamForm();
        when(service.createExamForm(anyInt())).thenReturn(form);

        mvc.perform(get("/professor/createExam")
                .param("mcCount", "1")
                .param("scCount", "1")
                .param("freitextCount", "1"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "username"))
            .andExpect(model().attributeExists("examForm"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(view().name("professor/createExam"));

        verify(service).createExamForm(3);
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist nicht sichtbar (Invalide Eingabedaten in ModelAttribute)")
    void showCreateExamForm_03() throws Exception {

        mvc.perform(get("/professor/createExam")
                .param("mcCount", "0")
                .param("scCount", "1")
                .param("freitextCount", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Invalide Eingabedaten!"))
            .andExpect(flash().attribute("success", false));

        verify(service, never()).createExamForm(2);
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist erfolgreich")
    void createExam_01() throws Exception {

        when(service.createExam(any(ExamForm.class), eq("username"))).thenReturn("");

        mvc.perform(post("/professor/createExam")
            .with(csrf())
                .param("title", "Test")
                .param("start", "2020-01-01T00:00")
                .param("end", "2020-01-01T01:00")
                .param("result", "2020-01-01T02:00")

                .param("questions[0].punkte", "1")
                .param("questions[0].type", "MC")
                .param("questions[0].questionText", "Text")
                .param("questions[0].choices", "Antwort1\nAntwort2")
                .param("questions[0].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[1].punkte", "1")
                .param("questions[1].type", "FREITEXT")
                .param("questions[1].questionText", "Text")

                .param("questions[2].punkte", "1")
                .param("questions[2].type", "SC")
                .param("questions[2].questionText", "Text")
                .param("questions[2].choices", "Antwort1\nAntwort2")
                .param("questions[2].correctAnswer", "Antwort1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Prüfung und Fragen erfolgreich erstellt!"))
            .andExpect(flash().attribute("success", true))
            .andExpect(request().sessionAttributeDoesNotExist("questionForm"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Eine Frage bekommt 0 Punkte")
    void createExam_03() throws Exception {

        MvcResult result = mvc.perform(post("/professor/createExam")
                .with(csrf())
                .param("title", "Test")
                .param("start", "2020-01-01T00:00")
                .param("end", "2020-01-01T01:00")
                .param("result", "2020-01-01T02:00")

                // Fragen 1 Punkte auf 0 gesetzt
                .param("questions[0].punkte", "0")
                .param("questions[0].type", "MC")
                .param("questions[0].questionText", "Text")
                .param("questions[0].choices", "Antwort1\nAntwort2")
                .param("questions[0].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[1].punkte", "1")
                .param("questions[1].type", "FREITEXT")
                .param("questions[1].questionText", "Text")

                .param("questions[2].punkte", "1")
                .param("questions[2].type", "SC")
                .param("questions[2].questionText", "Text")
                .param("questions[2].choices", "Antwort1\nAntwort2")
                .param("questions[2].correctAnswer", "Antwort1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Fehlerhafte Eingabedaten!"))
            .andExpect(flash().attribute("success", false))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);
        assertNotNull(session.getAttribute("questionForm"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Ein Exam mit der selben Startzeit existiert bereits / Maximale Kapazität ist überschritten worden")
    void createExam_04() throws Exception {

        when(service.createExam(any(ExamForm.class), eq("username"))).thenReturn("Error Nachricht");

        MvcResult result = mvc.perform(post("/professor/createExam")
                .with(csrf())
                .param("title", "Test")
                .param("start", "2020-01-01T00:00")
                .param("end", "2020-01-01T01:00")
                .param("result", "2020-01-01T02:00")

                .param("questions[0].punkte", "1")
                .param("questions[0].type", "MC")
                .param("questions[0].questionText", "Text")
                .param("questions[0].choices", "Antwort1\nAntwort2")
                .param("questions[0].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[1].punkte", "1")
                .param("questions[1].type", "FREITEXT")
                .param("questions[1].questionText", "Text")

                .param("questions[2].punkte", "1")
                .param("questions[2].type", "SC")
                .param("questions[2].questionText", "Text")
                .param("questions[2].choices", "Antwort1\nAntwort2")
                .param("questions[2].correctAnswer", "Antwort1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/questionSettings"))
            .andExpect(flash().attribute("message", "Error Nachricht"))
            .andExpect(flash().attribute("success", false))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);
        assertNotNull(session.getAttribute("questionForm"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Anzeige aller Prüfungen")
    void listExams() throws Exception {
        when(service.getAllExams()).thenReturn(List.of());

        mvc.perform(get("/professor/listExams"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/examListForProf"))
                .andExpect(model().attributeExists("exams"))
                .andExpect(model().attributeExists("timeNow"))
                .andExpect(model().attributeExists("currentPath"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Liste die Prüflinge für eine Prüfung auf (Ergebnisse sind vorhanden)")
    void listParticipants_01() throws Exception {
        ExamDTO exam = new ExamDTO(
                null,
                "",
                null,
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 9, 1)
        );

        when(service.getExamByUUID(any())).thenReturn(exam);
        when(service.getSubmitInfo(exam.id())).thenReturn(List.of());

        mvc.perform(get("/professor/listParticipants/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/submitStudentList"))
                .andExpect(model().attribute("exam", exam))
                .andExpect(model().attributeExists("submitInfoList"))
                .andExpect(model().attributeExists("timeNow"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Liste die Prüflinge für eine Prüfung auf (keine Ergebnisse sind vorhanden)")
    void listParticipants_02() throws Exception {
        ExamDTO exam = new ExamDTO(
                null,
                "",
                null,
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 1, 1, 11, 0)
        );

        when(service.getExamByUUID(any())).thenReturn(exam);

        mvc.perform(get("/professor/listParticipants/{examId}", UUID.randomUUID()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/listExams"))
                .andExpect(flash().attribute("message", "Ergebnisse noch nicht vorhanden!"))
                .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Ergebnis eines Studenten für eine Prüfung einsehbar")
    void showStudentResult() throws Exception {
        when(service.prepareReviewViewForm(any(), any())).thenReturn(mock());

        mvc.perform(get("/professor/showResult/{examId}/{studentName}", UUID.randomUUID(), "Student"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/showReview"))
                .andExpect(model().attributeExists("view"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Ein Exam kann gelöscht werden")
    void deleteExam_01() throws Exception {
        when(service.deleteExam(any())).thenReturn(true);

        mvc.perform(post("/professor/deleteExam/{examId}", UUID.randomUUID())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/listExams"))
                .andExpect(flash().attribute("success", true))
                .andExpect(flash().attribute("message", "Exam erfolgreich gelöscht!"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Ein laufendes Exam kann nicht gelöscht werden")
    void deleteExam_02() throws Exception {
        when(service.deleteExam(any())).thenReturn(false);

        mvc.perform(post("/professor/deleteExam/{examId}", UUID.randomUUID())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/listExams"))
                .andExpect(flash().attribute("success", false))
                .andExpect(flash().attribute("message", "Exam am laufen!"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Export einer Exam ist erfolgreich")
    void downloadExam_01() throws Exception {
        UUID examId = UUID.randomUUID();
        ExamDTO exam = mock(ExamDTO.class);
        when(service.getExamByUUID(examId)).thenReturn(exam);
        when(exam.title()).thenReturn("Title");

        ExamExportDTO export = new ExamExportDTO();
        when(service.getExamExport(examId)).thenReturn(List.of(export));

        byte[] csvBytes = "examTitle;points\nTitle;10\n".getBytes();
        when(csvExportService.exportExamToCsv(List.of(export))).thenReturn(csvBytes);

        mvc.perform(get("/professor/downloadExam/{examId}", examId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=\"Title.csv\""
                ))
                .andExpect(content().bytes(csvBytes));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Exception wird geworfen beim Download eines Exams")
    void downloadExam_02() throws Exception {
        UUID examId = UUID.randomUUID();

        when(service.getExamExport(examId))
                .thenThrow(new RuntimeException());

        mvc.perform(get("/professor/downloadExam/{examId}", examId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Export einer Bewertung ist erfolgreich")
    void downloadReview_01() throws Exception {
        UUID examId = UUID.randomUUID();
        ExamDTO exam = mock(ExamDTO.class);
        when(service.getExamByUUID(examId)).thenReturn(exam);
        when(exam.title()).thenReturn("Title");

        ReviewExportDTO export = new ReviewExportDTO();
        when(service.getReviewExport(examId, "Student")).thenReturn(List.of(export));

        byte[] csvBytes = "examTitle;points\nTitle;10\n".getBytes();
        when(csvExportService.exportReviewToCsv(List.of(export))).thenReturn(csvBytes);

        mvc.perform(get("/professor/downloadReview/{examId}/{studentName}", examId, "Student"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=\"Title_Student.csv\""
                ))
                .andExpect(content().bytes(csvBytes));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Exception wird geworfen beim Download eines Exams")
    void downloadReview_02() throws Exception {
        UUID examId = UUID.randomUUID();

        when(service.getReviewExport(examId, "Student"))
                .thenThrow(new RuntimeException());

        mvc.perform(get("/professor/downloadReview/{examId}/{studentName}", examId, "Student"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Vorschau eines Exams ist erfolgreich")
    void showExam() throws Exception {
        when(service.getExamView(any())).thenReturn(mock(ExamViewForm.class));

        mvc.perform(get("/professor/showExam/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exam"))
                .andExpect(view().name("professor/examView"));
    }
}