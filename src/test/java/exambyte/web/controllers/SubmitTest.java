package exambyte.web.controllers;

import exambyte.application.service.ExamManagementService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class SubmitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Das einreichen des Exams ist fehlgeschlagen")
    void test_01() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID frageId1 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        UUID frageId3 = UUID.randomUUID();

        when(examManagementService.submitExam(eq("Marvin0109"), any(), eq(examFachId))).thenReturn(false);

        mvc.perform(post("/exams/submit/{examFachId}", examFachId)
                .with(csrf())
                .param("answers[" + frageId1 + "]", "Antwort 1") // SC
                .param("answers[" + frageId2 + "]", "Antwort 1", "Antwort 2") // MC
                .param("answers[" + frageId3 + "]", "Dies ist meine Freitext Antwort"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Fehler beim Einreichen der Antworten."))
            .andExpect(flash().attribute("success", false))
            .andExpect(redirectedUrl("/exams/examsStudierende"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Das einreichen des Exams ist erfolgreich")
    void test_02() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID frageId1 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        UUID frageId3 = UUID.randomUUID();

        when(examManagementService.submitExam(eq("Marvin0109"), any(), eq(examFachId)))
                .thenReturn(true);

        mvc.perform(post("/exams/submit/{examFachId}", examFachId)
                .with(csrf())
                .param("answers[" + frageId1 + "]", "Antwort 1") // SC
                .param("answers[" + frageId2 + "]", "Antwort 1", "Antwort 2") // MC
                .param("answers[" + frageId3 + "]", "Dies ist meine Freitext Antwort"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Alle Antworten erfolgreich eingereicht!"))
            .andExpect(flash().attribute("success", true))
            .andExpect(redirectedUrl("/exams/examsStudierende"));
    }
}
