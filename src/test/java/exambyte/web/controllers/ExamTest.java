package exambyte.web.controllers;

import exambyte.ExamByteApplication;
import exambyte.domain.service.AppUserService;
import exambyte.domain.config.MethodSecurityConfig;
import exambyte.domain.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
@ContextConfiguration(classes = ExamByteApplication.class)
public class ExamTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @Autowired
    public ExamTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Test
    @DisplayName("Die exams Seite ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        mvc.perform(get("/exams"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109")
    @DisplayName("Die exams Seite ist für normale authentifizierte User nicht erreichbar")
    void test_02() throws Exception {
        mvc.perform(get("/exams"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"USER", "ADMIN"})
    @DisplayName("Die exams Seite ist sichtbar für Admins")
    void test_03() throws Exception {
        mvc.perform(get("/exams"))
                .andExpect(status().isOk());
    }
}
