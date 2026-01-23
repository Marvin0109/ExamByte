package exambyte.web.controllers;

import exambyte.application.service.ExamControllerService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({MethodSecurityConfig.class})
class IndexTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamControllerService service;

    @Test
    @DisplayName("Startseite ist öffentlich erreichbar")
    @WithMockUser
    void test_01() throws Exception {
        mvc.perform(get("/"))
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(status().isOk());
    }
}
