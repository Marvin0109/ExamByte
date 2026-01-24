package exambyte.web.controllers;

import exambyte.application.service.ExamControllerService;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class SettingsTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamControllerService service;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    @DisplayName("Die settings Seite nicht angemeldete User nicht zugänglich")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/settings"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT, REVIEWER, ADMIN"})
    @DisplayName("Die settings Seite ist für jeden angemeldeten User zugänglich")
    void test_02() throws Exception {
        mvc.perform(get("/settings"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("currentPath"))
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

    // TODO: settings/role Test
}
