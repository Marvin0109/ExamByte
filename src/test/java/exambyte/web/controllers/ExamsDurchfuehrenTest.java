package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.config.Oauth2Service;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.domain.service.*;
import exambyte.infrastructure.service.AppUserServiceImpl;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.domain.aggregate.exam.Exam;
import exambyte.infrastructure.service.*;
import exambyte.infrastructure.service.ExamManagementServiceImpl;
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
    private ExamManagementService examManagementService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private Oauth2Service oauth2Service;

    @MockBean
    private ExamDTOMapper examDTOMapper;

    @Autowired
    public ExamsDurchfuehrenTest(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
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

        ExamDTO dummyDTO = new ExamDTO(
                dummyExam.getId(),
                dummyExam.getFachId(),
                dummyExam.getTitle(),
                dummyExam.getProfessorFachId(),
                dummyExam.getStartTime(),
                dummyExam.getEndTime(),
                dummyExam.getResultTime());

        when(examManagementService.getExam(dummyExam.getFachId())).thenReturn(dummyExam);
        when(examDTOMapper.toDTO(dummyExam)).thenReturn(dummyDTO);

        mvc.perform(get("/api/exams/start/" + dummyDTO.fachId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("alreadySubmitted"))
                .andExpect(model().attribute("exam", dummyDTO))
                .andExpect(model().attribute("name", "Marvin0109"));
    }
}
