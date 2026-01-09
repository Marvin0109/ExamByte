package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsKorrektorTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerHelper helper;

    @Test
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void listExamsForReviewer_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsKorrektor"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"REVIEWER"})
    @DisplayName("Die Seite zur gesamten Korrekturübersicht ist erfolgreich")
    void listExamsForReviewer_02() throws Exception {

        mvc.perform(get("/exams/examsKorrektor"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "Marvin0109"))
            .andExpect(model().attributeExists("reviewCoverage"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(model().attributeExists("timeNow"))
            .andExpect(view().name("/exams/examsKorrektor"));
    }

    @Test
    @DisplayName("Seite zur Korrekturübersicht eines bestimmten Exams ist nicht erreichbar ohne Authentifizierung")
    void showExamSubmits_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/showExamSubmits/{examFachId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"REVIEWER"})
    @DisplayName("Seite zur Korrekturübersicht eines bestimmten Exams nicht erfolgreich: Exam läuft noch!")
    void showExamSubmits_02() throws Exception {

        UUID examFachId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.endTime()).thenReturn(LocalDateTime.now().plusDays(1));

        when(helper.getExamByUUID(examFachId)).thenReturn(examDTO);

        mvc.perform(get("/exams/showExamSubmits/{examFachId}", examFachId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Die Prüfung läuft noch! Keine Korrektur erlaubt."))
                .andExpect(flash().attribute("success", false))
                .andExpect(redirectedUrl("/exams/examsKorrektor"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"REVIEWER"})
    @DisplayName("Seite zur Korrekturübersicht erfolgt")
    void showExamSubmits_03() throws Exception {

        UUID examFachId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.endTime()).thenReturn(LocalDateTime.now().minusDays(1));

        when(helper.getExamByUUID(examFachId)).thenReturn(examDTO);
        when(helper.getSubmitInfo(examFachId)).thenReturn(List.of());

        mvc.perform(get("/exams/showExamSubmits/{examFachId}", examFachId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("exam", examDTO))
                .andExpect(model().attributeExists("submitInfoList"))
                .andExpect(model().attributeExists("timeNow"))
                .andExpect(view().name("/exams/examSubmittedUebersicht"));
    }

    @Test
    @DisplayName("Seite zur Korrektur erfolgt nicht ohne Authentifizierung")
    void showSubmit_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/showSubmit/{examFachId}/{studentFachId}",
                        UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    // TODO: Anpassen, HTML Seite existiert noch nicht!
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"REVIEWER"})
    @DisplayName("Korrekturseite erfolgt")
    void showExamSubmits_04() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID studentFachId = UUID.randomUUID();
        mvc.perform(get("/exams/showExamSubmits/{examFachId}/{studentFachId}", examFachId, studentFachId))
            .andExpect(status().is4xxClientError());
    }
}