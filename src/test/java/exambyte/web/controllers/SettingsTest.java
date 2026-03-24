package exambyte.web.controllers;

import exambyte.web.service.ExamControllerService;
import exambyte.application.service.user.UserCreationService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.application.service.user.AppUserService;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
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
    private UserCreationService creationService;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    void get_settings_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/settings"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT", "REVIEWER", "ADMIN"})
    void get_settings_authorized() throws Exception {
        mvc.perform(get("/settings"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(view().name("settings"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_settings_authorized_withOnlyOneRole() throws Exception {
        mvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPath"))
                .andExpect(view().name("settings"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    void post_rest_success() throws Exception {
        when(service.reset()).thenReturn(true);

        mvc.perform(post("/settings/reset")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Daten wurden erfolgreich gelöscht!"))
            .andExpect(flash().attribute("success", true))
            .andExpect(redirectedUrl("/settings"));
    }

    @Test
    @WithMockOAuth2User(roles = {"ADMIN"})
    void post_rest_fail() throws Exception {
        when(service.reset()).thenReturn(false);

        mvc.perform(post("/settings/reset")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Zulassungsszenario am laufen!"))
            .andExpect(flash().attribute("success", false))
            .andExpect(redirectedUrl("/settings"));
    }
}
