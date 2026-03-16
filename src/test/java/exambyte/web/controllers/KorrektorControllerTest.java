package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.AppUserService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.application.service.ExamControllerService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KorrektorController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class KorrektorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @Test
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void listExamsForReviewer_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/korrektor/examListForReviewer"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Die Seite zur gesamten Korrekturübersicht ist erfolgreich")
    void listExamsForReviewer_02() throws Exception {

        mvc.perform(get("/korrektor/examListForReviewer"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "username"))
            .andExpect(model().attributeExists("reviewCoverage"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(model().attributeExists("timeNow"))
            .andExpect(view().name("korrektor/examListForReviewer"));
    }

    @Test
    @DisplayName("Seite zur Korrekturübersicht eines bestimmten Exams ist nicht erreichbar ohne Authentifizierung")
    void showExamSubmits_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/korrektor/showExamSubmits/{examId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Seite zur Korrekturübersicht eines bestimmten Exams nicht erfolgreich: Exam läuft noch!")
    void showExamSubmits_02() throws Exception {

        UUID examId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.endTime()).thenReturn(LocalDateTime.now().plusDays(1));

        when(service.getExamByUUID(examId)).thenReturn(examDTO);

        mvc.perform(get("/korrektor/showExamSubmits/{examId}", examId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Die Prüfung läuft noch! Keine Korrektur erlaubt."))
                .andExpect(flash().attribute("success", false))
                .andExpect(redirectedUrl("/korrektor/examListForReviewer"));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Seite zur Korrekturübersicht erfolgt")
    void showExamSubmits_03() throws Exception {

        UUID examId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.endTime()).thenReturn(LocalDateTime.now().minusDays(1));

        when(service.getExamByUUID(examId)).thenReturn(examDTO);
        when(service.getSubmitInfo(examId)).thenReturn(List.of());

        mvc.perform(get("/korrektor/showExamSubmits/{examId}", examId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("exam", examDTO))
                .andExpect(model().attributeExists("submitInfoList"))
                .andExpect(model().attributeExists("timeNow"))
                .andExpect(view().name("korrektor/examSubmitsView"));
    }

    @Test
    @DisplayName("Seite zur Korrektur erfolgt nicht ohne Authentifizierung")
    void showSubmit_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/korrektor/showSubmit/{examId}/{studentId}",
                        UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Korrekturseite ist erreichbar")
    void showSubmit_02() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        mvc.perform(get("/korrektor/showSubmit/{examId}/{studentId}", examId, studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("antworten"))
            .andExpect(model().attributeExists("reviewForm"))
            .andExpect(view().name("korrektor/showSubmit"));
    }

    @Test
    @DisplayName("Das erstellen der Reviews erfolgt nicht ohne Authentifizierung")
    void createReview_01() throws Exception {
        mvc.perform(post("/korrektor/createReview/{antwortId}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Erstellen einer Bewertung ist erfolgreich")
    void createReview_02() throws Exception {
        mvc.perform(post("/korrektor/createReview/{antwortId}", UUID.randomUUID())
                .with(csrf())
                .param("bewertung", "Bewertung")
                .param("punkteVergeben", "1.5"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/korrektor/examListForReviewer"))
            .andExpect(flash().attribute("message", "Bewertung erfolgreich!"))
            .andExpect(flash().attribute("success", true));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Erstellen einer Bewertung schlägt fehl (Bewertungstext fehlt)")
    void createReview_03() throws Exception {
        mvc.perform(post("/korrektor/createReview/{antwortId}", UUID.randomUUID())
                .with(csrf())
                .param("bewertung", "")
                .param("punkteVergeben", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/korrektor/examListForReviewer"))
            .andExpect(flash().attribute("message", "Ein Bewertungstext muss vorhanden sein"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Erstellen einer Bewertung schlägt fehl (Ungültige Punktzahl vergeben)")
    void createReview_04() throws Exception {
        mvc.perform(post("/korrektor/createReview/{antwortId}", UUID.randomUUID())
                .with(csrf())
                .param("bewertung", "B")
                .param("punkteVergeben", "-1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/korrektor/examListForReviewer"))
            .andExpect(flash().attribute("message", "Punkte dürfen nicht negativ sein"))
            .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    @DisplayName("Erstellen einer Bewertung schlägt fehl (Ungültige Punktzahl vergeben)")
    void createReview_05() throws Exception {
        mvc.perform(post("/korrektor/createReview/{antwortId}", UUID.randomUUID())
                .with(csrf())
                .param("bewertung", "B")
                .param("punkteVergeben", "0.25"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/korrektor/examListForReviewer"))
            .andExpect(flash().attribute("message", "Nur halbe Punkte erlaubt (0.5 Schritte)"))
            .andExpect(flash().attribute("success", false));
    }
}