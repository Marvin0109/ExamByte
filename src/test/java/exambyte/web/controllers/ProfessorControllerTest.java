package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.AppUserService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.application.service.ExamControllerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
        when(service.createExamForm()).thenReturn(form);

        mvc.perform(get("/professor/createExam"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "username"))
            .andExpect(model().attributeExists("examForm"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(view().name("professor/createExam"));
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

                .param("questions[3].punkte", "1")
                .param("questions[3].type", "MC")
                .param("questions[3].questionText", "Text")
                .param("questions[3].choices", "Antwort1\nAntwort2")
                .param("questions[3].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[4].punkte", "1")
                .param("questions[4].type", "SC")
                .param("questions[4].questionText", "Text")
                .param("questions[4].choices", "Antwort1\nAntwort2")
                .param("questions[4].correctAnswer", "Antwort1")

                .param("questions[5].punkte", "1")
                .param("questions[5].type", "FREITEXT")
                .param("questions[5].questionText", "Text")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/createExam"))
            .andExpect(flash().attribute("message", "Prüfung und Fragen erfolgreich erstellt!"))
            .andExpect(flash().attribute("success", true));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests mit zu wenig Fragen ist nicht erfolgreich")
    void createExam_02() throws Exception {

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
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/createExam"))
            .andExpect(flash().attribute("message", "Weniger Fragen als sonst."))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Eine Frage bekommt 0 Punkte")
    void createExam_03() throws Exception {

        mvc.perform(post("/professor/createExam")
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

                .param("questions[3].punkte", "1")
                .param("questions[3].type", "MC")
                .param("questions[3].questionText", "Text")
                .param("questions[3].choices", "Antwort1\nAntwort2")
                .param("questions[3].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[4].punkte", "1")
                .param("questions[4].type", "SC")
                .param("questions[4].questionText", "Text")
                .param("questions[4].choices", "Antwort1\nAntwort2")
                .param("questions[4].correctAnswer", "Antwort1")

                .param("questions[5].punkte", "1")
                .param("questions[5].type", "FREITEXT")
                .param("questions[5].questionText", "Text")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/createExam"))
            .andExpect(flash().attribute("message", "Fehlerhafte Eingabedaten!"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    @DisplayName("Ein Exam mit der selben Startzeit existiert bereits / Maximale Kapazität ist überschritten worden")
    void createExam_04() throws Exception {

        when(service.createExam(any(ExamForm.class), eq("username"))).thenReturn("Error Nachricht");

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

                .param("questions[3].punkte", "1")
                .param("questions[3].type", "MC")
                .param("questions[3].questionText", "Text")
                .param("questions[3].choices", "Antwort1\nAntwort2")
                .param("questions[3].correctAnswers", "Antwort1\nAntwort2")

                .param("questions[4].punkte", "1")
                .param("questions[4].type", "SC")
                .param("questions[4].questionText", "Text")
                .param("questions[4].choices", "Antwort1\nAntwort2")
                .param("questions[4].correctAnswer", "Antwort1")

                .param("questions[5].punkte", "1")
                .param("questions[5].type", "FREITEXT")
                .param("questions[5].questionText", "Text")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/professor/createExam"))
            .andExpect(flash().attribute("message", "Error Nachricht"))
            .andExpect(flash().attribute("success", false));
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
        when(service.getSubmitInfo(exam.fachId())).thenReturn(List.of());

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
}