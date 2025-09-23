package exambyte.web.controllers;

import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.service.AppUserService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ContactTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    @DisplayName("Die contact Seite ist für nicht-authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/contact"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT", "REVIEWER", "ADMIN"})
    @DisplayName("Die contact Seite ist für authentifizierte User erreichbar, die eine höhere Rolle haben als USER")
    void test_02() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        when(examManagementService.getProfFachIDByName("Marvin0109")).thenReturn(Optional.of(randomUUID));

        mvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attribute("name", "Marvin0109"))
                .andExpect(model().attribute("fachID", randomUUID.toString()))
                .andExpect(model().attributeExists("currentPath"));
    }
}