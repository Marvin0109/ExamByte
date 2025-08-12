package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.application.service.ExamManagementService;
import exambyte.domain.service.*;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.ExamForm;
import exambyte.web.form.QuestionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    private ExamService examService;

    @MockitoBean
    private ProfessorService professorService;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private FrageService frageService;

    @MockitoBean
    private ExamDTOMapper examDTOMapper;

    @Test
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsProfessoren"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für Professoren sichtbar")
    void test_02() throws Exception {
        mvc.perform(get("/exams/examsProfessoren"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Marvin0109"))
                .andExpect(model().attributeExists("examForm"))
                .andExpect(view().name("/exams/examsProfessoren"));
    }

    // TODO
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist nicht erfolgreich")
    void test_03() throws Exception {

    }

    // TODO
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist erfolgreich")
    void test_04() throws Exception {

    }

    // TODO
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen der Fragen ist erfolgreich")
    void test_05() throws Exception {

    }

    // TODO
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen der Fragen schlägt fehl")
    void test_06() throws Exception {

    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das löschen aller Exam Daten ist erfolgreich")
    void test_07() throws Exception {
        mvc.perform(post("/exams/examsProfessoren/reset")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exams/examsProfessoren"));

        verify(examManagementService).reset();
    }
}