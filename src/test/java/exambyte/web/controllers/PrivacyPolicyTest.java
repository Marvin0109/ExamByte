package exambyte.web.controllers;

import exambyte.application.service.user.AppUserService;
import exambyte.application.service.user.UserCreationService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.web.service.ExamControllerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class PrivacyPolicyTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamControllerService service;

    @MockitoBean
    private UserCreationService creationService;

    @MockitoBean
    private AppUserService userService;

    @Test
    void get_privacyPolicy_success() throws Exception {
        mvc.perform(get("/privacyPolicy"))
            .andExpect(view().name("privacyPolicy"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("currentPath"));
    }
}
