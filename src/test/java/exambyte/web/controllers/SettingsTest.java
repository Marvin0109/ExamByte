package exambyte.web.controllers;

import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.AppUserService;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class SettingsTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"USER", "STUDENT", "REVIEWER"})
    @DisplayName("Die settings Seite ist für jeden außer dem Admin nicht zugänglich")
    void test_01() throws Exception {
        mvc.perform(get("/settings"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Die settings Seite ist für Admins zugänglich")
    void test_02() throws Exception {
        mvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das löschen der Daten ist erfolgreich")
    void test_03() throws Exception {
        mvc.perform(post("/settings/reset")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Daten wurden erfolgreich gelöscht!"))
                .andExpect(flash().attribute("success", true))
                .andExpect(redirectedUrl("/settings"));
    }
}
