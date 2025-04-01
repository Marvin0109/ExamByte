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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsKorrektorTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamManagementService examManagementService;

    @Test
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsKorrektor"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"REVIEWER"})
    @DisplayName("Die Seite zum Korrigieren von Prüfungen ist für Korrektoren sichtbar")
    void test_02() throws Exception {
        when(examManagementService.getAllExams()).thenReturn(List.of());

        List<ExamDTO> examDTOs = examManagementService.getAllExams();

        mvc.perform(get("/exams/examsKorrektor"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Marvin0109"))
                .andExpect(model().attribute("exams", examDTOs))
                .andExpect(view().name("/exams/examsKorrektor"));
    }
}