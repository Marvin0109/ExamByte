package exambyte.web.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ContactTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @Autowired
    public ContactTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

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
    @WithMockOAuth2User(login = "Marvin0109", roles = {"USER", "STUDENT", "REVIEWER", "ADMIN"})
    @DisplayName("Die contact Seite ist für authentifizierte User erreichbar")
    void test_02() throws Exception {
        mvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Marvin0109"));
    }
}