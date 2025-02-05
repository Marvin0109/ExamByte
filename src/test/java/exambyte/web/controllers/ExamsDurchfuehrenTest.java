package exambyte.web.controllers;

import exambyte.application.service.AppUserServiceImpl;
import exambyte.application.config.MethodSecurityConfig;
import exambyte.application.config.SecurityConfig;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.service.impl.*;
import exambyte.application.service.ExamManagementServiceImpl;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

    @MockBean
    private ExamManagementServiceImpl examManagementServiceImpl;

    @MockBean
    private ExamServiceImpl examServiceImpl;

    @MockBean
    private AntwortServiceImpl antwortServiceImpl;

    @MockBean
    private ProfessorServiceImpl professorServiceImpl;

    @MockBean
    private StudentServiceImpl studentServiceImpl;

    @MockBean
    private FrageServiceImpl frageServiceImpl;

    @MockBean
    private AppUserServiceImpl appUserServiceImpl;

    @Autowired
    public ExamsDurchfuehrenTest(AppUserServiceImpl appUserServiceImpl) {
        this.appUserServiceImpl = appUserServiceImpl;
    }

    @Test
    @DisplayName("Die Seite zum Durchführen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void test_01() throws Exception {
        UUID examDummyFachId = UUID.randomUUID();
        MvcResult mvcResult = mvc.perform(get("/api/exams/start/" + examDummyFachId))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
                .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Diese Seite ist für Studierende sichtbar")
    void test_02() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, 2, 28, 23, 59);
        LocalDateTime resultTime = LocalDateTime.of(2020, 3, 31, 23, 59);
        Exam dummyExam = new Exam.ExamBuilder()
                .id(null)
                .fachId(null)
                .title("Dummy Exam")
                .professorFachId(UUID.randomUUID())
                .startTime(startTime)
                .endTime(endTime)
                .resultTime(resultTime)
                .build();

        when(examServiceImpl.getExam(dummyExam.getFachId())).thenReturn(dummyExam);

        mvc.perform(get("/api/exams/start/" + dummyExam.getFachId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("alreadySubmitted"))
                .andExpect(model().attribute("exam", dummyExam))
                .andExpect(model().attribute("name", "Marvin0109"));
    }
}
