package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.service.ExamManagementService;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsDurchfuehrenTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private AppUserService appUserService;

    @Test
    @DisplayName("Die Seite zum Durchführen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        UUID examDummyFachId = UUID.randomUUID();
        MvcResult mvcResult = mvc.perform(get("/exams/examsDurchfuehren" + examDummyFachId))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Diese Seite ist für Studierende sichtbar")
    void test_02() throws Exception {
        UUID fachID = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 2, 28, 23, 59);
        LocalDateTime resultTime = LocalDateTime.of(2020, 3, 31, 23, 59);

        ExamDTO dummyDTO = new ExamDTO(
                null,
                fachID,
                "Dummy Exam",
                UUID.randomUUID(),
                startTime,
                endTime,
                resultTime);

        when(examManagementService.getExam(dummyDTO.fachId())).thenReturn(dummyDTO);

        mvc.perform(get("/exams/examsDurchfuehren/{examFachId}", fachID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exam"))
                .andExpect(model().attribute("name", "Marvin0109"))
                .andExpect(view().name("/exams/examsDurchfuehren"));
    }
}
