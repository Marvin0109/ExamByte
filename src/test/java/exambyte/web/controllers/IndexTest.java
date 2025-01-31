package exambyte.web.controllers;

import exambyte.ExamByteApplication;
import exambyte.domain.config.MethodSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebController.class)
@Import({MethodSecurityConfig.class})
@ContextConfiguration(classes = ExamByteApplication.class)
public class IndexExam {

    private final MockMvc mvc;
    @Autowired
    public IndexExam(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("Startseite ist öffentlich erreichbar")
    @WithMockUser
    void test_01() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
