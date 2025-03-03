package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.service.*;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsProfessorenTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ExamService examService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private ExamManagementService examManagementService;

    @MockBean
    private ExamDTOMapper examDTOMapper;

    @Test
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsProfessoren"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für Professoren sichtbar")
    void test_02() throws Exception {
        mvc.perform(get("/exams/examsProfessoren"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Marvin0109"))
                .andExpect(view().name("/exams/examsProfessoren"));
    }

    @Disabled
    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist erfolgreich")
    void test_03() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 1, 31, 23, 59);
        LocalDateTime resultTime = LocalDateTime.of(2020, 2, 1, 0, 0);
        UUID profFachId = UUID.randomUUID();
        Optional<UUID> optProfFachId = Optional.of(profFachId);
        ExamDTO examDTO = new ExamDTO(null, null, "Test 1", profFachId, startTime, endTime, resultTime);

        when(examManagementService.createExam("Marvin0109", "Test 1", startTime, endTime, resultTime))
                .thenReturn(true);
        when(professorService.getProfessorFachId("Marvin0109")).thenReturn(optProfFachId);
        mvc.perform(post("/exams/examsProfessoren")
                    .param("title", "Test 1")
                    .param("startTime", startTime.toString())
                    .param("endTime", endTime.toString())
                    .param("resultTime", resultTime.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("message", "Test erfolgreich erstellt!"))
                .andExpect(model().attribute("exam", examDTO))
                .andExpect(redirectedUrl("/exams/examsProfessoren"));

    }
}