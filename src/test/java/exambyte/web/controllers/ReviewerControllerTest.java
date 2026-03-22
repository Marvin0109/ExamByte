package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.AppUserService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.application.service.ExamControllerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

@WebMvcTest(ReviewerController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class ReviewerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @Test
    void get_listExamsForReviewer_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/reviewer/examListForReviewer"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void get_listExamsForReviewer_success() throws Exception {

        mvc.perform(get("/reviewer/examListForReviewer"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("name", "username"))
            .andExpect(model().attributeExists("reviewCoverage"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(model().attributeExists("timeNow"))
            .andExpect(view().name("reviewer/examListForReviewer"));
    }

    @Test
    void get_showExamSubmits_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/reviewer/showExamSubmits/{examId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void get_showExamSubmits_fail() throws Exception {
        UUID examId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.end()).thenReturn(LocalDateTime.now().plusDays(1));

        when(service.getExamByUUID(examId)).thenReturn(examDTO);

        mvc.perform(get("/reviewer/showExamSubmits/{examId}", examId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Die Prüfung läuft noch! Keine Korrektur erlaubt."))
                .andExpect(flash().attribute("success", false))
                .andExpect(redirectedUrl("/reviewer/examListForReviewer"));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void get_showExamSubmits_success() throws Exception {
        UUID examId = UUID.randomUUID();

        ExamDTO examDTO = mock(ExamDTO.class);
        when(examDTO.end()).thenReturn(LocalDateTime.now().minusDays(1));

        when(service.getExamByUUID(examId)).thenReturn(examDTO);
        when(service.getSubmitInfo(examId)).thenReturn(List.of());

        mvc.perform(get("/reviewer/showExamSubmits/{examId}", examId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("exam", examDTO))
                .andExpect(model().attributeExists("submitInfoList"))
                .andExpect(model().attributeExists("timeNow"))
                .andExpect(view().name("reviewer/examSubmitsView"));
    }

    @Test
    void get_showSubmit_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/reviewer/showSubmit/{examId}/{studentId}",
                        UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void get_showSubmit_success() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        mvc.perform(get("/reviewer/showSubmit/{examId}/{studentId}", examId, studentId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("answers"))
            .andExpect(model().attributeExists("reviewForm"))
            .andExpect(view().name("reviewer/showSubmit"));
    }

    @Test
    void post_createReview_notAuthorized() throws Exception {
        mvc.perform(post("/reviewer/createReview/{answerId}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void post_createReview_success() throws Exception {
        mvc.perform(post("/reviewer/createReview/{answerId}", UUID.randomUUID())
                .with(csrf())
                .param("reviewText", "Text")
                .param("points", "1.5"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/reviewer/examListForReviewer"))
            .andExpect(flash().attribute("message", "Bewertung erfolgreich!"))
            .andExpect(flash().attribute("success", true));
    }

    @Test
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void post_createReview_fail() throws Exception {
        mvc.perform(post("/reviewer/createReview/{answerId}", UUID.randomUUID())
                .with(csrf())
                .param("reviewText", "")
                .param("points", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/reviewer/examListForReviewer"))
            .andExpect(flash().attribute("message", "Ein Bewertungstext muss vorhanden sein"))
            .andExpect(flash().attribute("success", false));
    }

    @ParameterizedTest(name = "points={0} -> message={1}")
    @CsvSource({
            "-1, Punkte dürfen nicht negativ sein",
            "0.25, Nur halbe Punkte erlaubt (0.5 Schritte)",
            "'', Punkte müssen angegeben werden"
    })
    @WithMockOAuth2User(roles = {"REVIEWER"})
    void createReview_parameterizedTest(String points, String expectedMessage) throws Exception {
        mvc.perform(post("/reviewer/createReview/{answerId}", UUID.randomUUID())
                .with(csrf())
                .param("reviewText", "B")
                .param("points", points))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/reviewer/examListForReviewer"))
            .andExpect(flash().attribute("message", expectedMessage))
            .andExpect(flash().attribute("success", false));
    }
}