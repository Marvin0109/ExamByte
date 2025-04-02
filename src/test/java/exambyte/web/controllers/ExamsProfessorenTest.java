package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.domain.mapper.ExamDTOMapper;
import exambyte.application.service.ExamManagementService;
import exambyte.domain.service.*;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.infrastructure.service.*;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.ExamForm;
import exambyte.web.form.FrageForm;
import exambyte.web.form.helper.QuestionType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.array;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class ExamsProfessorenTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamService examService;

    @MockitoBean
    private ProfessorService professorService;

    @MockitoBean
    private ExamManagementService examManagementService;

    @MockitoBean
    private FrageService frageService;

    @MockitoBean
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
                .andExpect(model().attributeExists("examForm"))
                .andExpect(model().attributeExists("frageForm"))
                .andExpect(view().name("/exams/examsProfessoren"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist nicht erfolgreich")
    void test_03() throws Exception {
        when(examManagementService.createExam(any(), any(), any(), any(), any())).thenReturn(false);
        when(examManagementService.getProfFachIDByName(any())).thenReturn(Optional.of(UUID.randomUUID()));
        when(examManagementService.getExamByStartTime(any())).thenReturn(UUID.randomUUID());

        ExamForm examForm = new ExamForm();
        examForm.setTitle("Test");
        examForm.setStart(LocalDateTime.now());
        examForm.setEnd(LocalDateTime.now().plusDays(1));
        examForm.setResult(LocalDateTime.now().plusDays(2));

        FrageForm frageForm = new FrageForm();
        frageForm.setTitle("Test");
        frageForm.setFrageText("Frage 1");
        frageForm.setType(QuestionType.FREITEXT);
        frageForm.setMaxPunkte(2);
        frageForm.setChoices(List.of("Choice 1", "Choice 2", "Choice 3"));
        frageForm.setCorrectAnswer("Correct");
        frageForm.setCorrectAnswers(List.of("Correct 1", "Correct 2"));

        examForm.setFragen(List.of(frageForm));

        mvc.perform(post("/exams/examsProfessoren")
                        .flashAttr("examForm", examForm)
                        .flashAttr("frageForm", frageForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exams/examsProfessoren"))
                .andExpect(flash().attribute("message", "Fehler beim Erstellen der Prüfung."));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"ADMIN"})
    @DisplayName("Das erstellen eines Tests ist erfolgreich")
    void test_04() throws Exception {
        Optional<UUID> profFachID = Optional.of(UUID.randomUUID());
        UUID examUUID = UUID.randomUUID();

        when(examManagementService.createExam(any(), any(), any(), any(), any())).thenReturn(true);
        when(examManagementService.getProfFachIDByName(any())).thenReturn(profFachID);
        when(examManagementService.getExamByStartTime(any())).thenReturn(examUUID);

        ExamForm examForm = new ExamForm();
        examForm.setTitle("Test");
        examForm.setStart(LocalDateTime.now());
        examForm.setEnd(LocalDateTime.now().plusDays(1));
        examForm.setResult(LocalDateTime.now().plusDays(2));

        FrageForm frageForm = new FrageForm();
        frageForm.setTitle("Test");
        frageForm.setFrageText("Frage 1");
        frageForm.setType(QuestionType.FREITEXT);
        frageForm.setMaxPunkte(2);
        frageForm.setChoices(List.of("Choice 1", "Choice 2", "Choice 3"));
        frageForm.setCorrectAnswer("Correct");
        frageForm.setCorrectAnswers(List.of("Correct 1", "Correct 2"));

        examForm.setFragen(List.of(frageForm));

        ExamDTO exam = new ExamDTO(null, null, examForm.getTitle(), profFachID.get(), examForm.getStart(),
                examForm.getEnd(), examForm.getResult());

        mvc.perform(post("/exams/examsProfessoren")
                        .flashAttr("examForm", examForm)
                        .flashAttr("frageForm", frageForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exams/examsProfessoren"))
                .andExpect(flash().attribute("exam", exam))
                .andExpect(flash().attribute("message", "Test erfolgreich erstellt!"));
    }
}