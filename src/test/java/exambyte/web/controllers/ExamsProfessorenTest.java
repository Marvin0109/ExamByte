package exambyte.web.controllers;

import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.ExamForm;
import exambyte.application.service.ExamControllerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsProfessorenTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @Test
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void showCreateExamForm_01() throws Exception {

        MvcResult mvcResult = mvc.perform(get("/exams/examsProfessoren"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für Professoren sichtbar")
    void showCreateExamForm_02() throws Exception {

        ExamForm form = new ExamForm();
        when(service.createExamForm()).thenReturn(form);

        mvc.perform(get("/exams/examsProfessoren"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "Marvin0109"))
            .andExpect(model().attributeExists("examForm"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(view().name("/exams/examsProfessoren"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist erfolgreich")
    void createExam_01() throws Exception {

        when(service.createExam(any(ExamForm.class), eq("Marvin0109"))).thenReturn("");

        mvc.perform(post("/exams/examsProfessoren")
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
            .andExpect(redirectedUrl("/exams/examsProfessoren"))
            .andExpect(flash().attribute("message", "Prüfung und Fragen erfolgreich erstellt!"))
            .andExpect(flash().attribute("success", true));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests mit zu wenig Fragen ist nicht erfolgreich")
    void createExam_02() throws Exception {

        mvc.perform(post("/exams/examsProfessoren")
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
            .andExpect(redirectedUrl("/exams/examsProfessoren"))
            .andExpect(flash().attribute("message", "Weniger Fragen als sonst."))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Eine Frage bekommt 0 Punkte")
    void createExam_03() throws Exception {

        mvc.perform(post("/exams/examsProfessoren")
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
            .andExpect(redirectedUrl("/exams/examsProfessoren"))
            .andExpect(flash().attribute("message", "Fehlerhafte Eingabedaten!"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Ein Exam mit der selben Startzeit existiert bereits / Maximale Kapazität ist überschritten worden")
    void createExam_04() throws Exception {

        when(service.createExam(any(ExamForm.class), eq("Marvin0109"))).thenReturn("Error Nachricht");

        mvc.perform(post("/exams/examsProfessoren")
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
            .andExpect(redirectedUrl("/exams/examsProfessoren"))
            .andExpect(flash().attribute("message", "Error Nachricht"))
            .andExpect(flash().attribute("success", false));
    }
}