package exambyte.main.controllers;

import exambyte.main.config.MethodSecurityConfig;
import exambyte.main.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class IndexTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Startseite ist öffentlich erreichbar")
    void test_01() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
