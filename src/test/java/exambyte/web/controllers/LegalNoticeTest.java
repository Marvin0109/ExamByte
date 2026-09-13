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
class LegalNoticeTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamControllerService service;

    @MockitoBean
    private UserCreationService creationService;

    @MockitoBean
    private AppUserService userService;

    @Test
    void get_legal_notice_success() throws Exception{
        mvc.perform(get("/legalNotice"))
            .andExpect(view().name("legalNotice"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(status().isOk());
    }
}
