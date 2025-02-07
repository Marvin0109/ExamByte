package exambyte.web.controllers;

import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.service.*;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    private AntwortService antwortService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private FrageService frageService;

    @MockBean
    private ExamManagementService examManagementService;

    @MockBean
    private ExamDTOMapper examDTOMapper;

    @Autowired
    public ExamsProfessorenTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Test
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/exams/create"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Die Seite zum Erstellen von Prüfungen ist für Professoren sichtbar")
    void test_02() throws Exception {
        mvc.perform(get("/api/exams/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Marvin0109"));
    }
}