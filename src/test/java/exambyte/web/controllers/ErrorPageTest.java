package exambyte.web.controllers;

import exambyte.infrastructure.config.MethodSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ErrorPageController.class)
@Import({MethodSecurityConfig.class})
class ErrorPageTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void test404Page() throws Exception {
        mvc.perform(get("/error")
                .requestAttr("javax.servlet.error.status_code", 404)
                .requestAttr("javax.servlet.error.message", "Resource Not Found"))
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("status", 404))
            .andExpect(model().attribute("error", "Resource Not Found"));
    }
}
