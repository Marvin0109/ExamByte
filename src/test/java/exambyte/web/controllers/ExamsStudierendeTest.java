package exambyte.web.controllers;

import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.service.*;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.AppUserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsStudierendeTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ExamDTOMapper examDTOMapper;

    @MockBean
    private ExamManagementService examManagementService;

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

    @Autowired
    public ExamsStudierendeTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Test
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsStudierende"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }


    @Test
    @WithMockOAuth2User(login = "Student", roles = {"STUDENT"})
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für Studierende sichtbar")
    void test_02() throws Exception {
        mvc.perform(get("/exams/examsStudierende"))
                .andExpect(status().isOk())
                //.andExpect(view().name("exams/examsStudierende"))
                .andExpect(model().attributeExists("exams"));
    }
}