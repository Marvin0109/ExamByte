package exambyte.web.controllers;

import exambyte.ExamByteApplication;
import exambyte.application.service.AppUserService;
import exambyte.application.config.MethodSecurityConfig;
import exambyte.application.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
@ContextConfiguration(classes = ExamByteApplication.class)
public class ExamsKorrektorTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @Autowired
    public ExamsKorrektorTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
    @Test
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsKorrektor"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }


    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"USER", "KORREKTOR"})
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für Korrektoren sichtbar")
    void test_02() throws Exception {
        mvc.perform(get("/exams/examsKorrektor"))
                .andExpect(model().attribute("name", "Marvin0109"));
    }
}