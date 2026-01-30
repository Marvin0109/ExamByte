package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.application.service.AppUserService;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.ExamForm;
import exambyte.web.form.ExamTimeInfo;
import exambyte.web.form.SubmitForm;
import exambyte.application.service.ExamControllerService;
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
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class ExamsStudierendeTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @Test
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void listExamsForStudents_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsStudierende"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für Studierende sichtbar")
    void listExamsForStudents_02() throws Exception {

        when(service.getAllExams()).thenReturn(List.of());

        mvc.perform(get("/exams/examsStudierende"))
            .andExpect(status().isOk())
            .andExpect(view().name("exams/examsStudierende"))
            .andExpect(model().attribute("name", "Marvin0109"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(model().attributeExists("exams"))
            .andExpect(model().attributeExists("timeNow"));
    }

    @Test
    @DisplayName("Prüfungsmenü nicht erreichbar für nicht authentifizierte User")
    void examMenu_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsDurchfuehren/{examFachId}/menu", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Prüfungsmenü ist erreichbar (noch kein Exam eingereicht vorher)")
    void examMenu_02() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Anzeige", true);
        ExamDTO examDTO = new ExamDTO(examFachId, "Exam 1", profFachId,
                start, start.plusHours(1), start.plusHours(2));
        ProfessorDTO p = new ProfessorDTO(profFachId, "ProfName");

        when(service.getExamByUUID(examFachId)).thenReturn(examDTO);
        when(service.examIsAlreadySubmitted(examFachId, "Marvin0109")).thenReturn(false);
        when(service.getExamTimeInfo(examDTO)).thenReturn(examTimeInfo);
        when(service.getProfessorByFachId(profFachId)).thenReturn(p);

        mvc.perform(get("/exams/examsDurchfuehren/{examFachId}/menu", examFachId))
            .andExpect(status().isOk())
            .andExpect(view().name("exams/examMenu"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attribute("timeLeft", "Anzeige"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", false))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Prüfungsmenü ist erreichbar (Exam eingereicht vorher)")
    void examMenu_03() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID profFachId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Anzeige", true);
        ExamDTO examDTO = new ExamDTO(examFachId, "Exam 1", profFachId,
                start, start.plusHours(1), start.plusHours(2));
        ProfessorDTO p = new ProfessorDTO(profFachId, "ProfName");
        VersuchDTO versuchDTO = mock(VersuchDTO.class);

        when(service.getExamByUUID(examFachId)).thenReturn(examDTO);
        when(service.examIsAlreadySubmitted(examFachId, "Marvin0109")).thenReturn(true);
        when(service.getExamTimeInfo(examDTO)).thenReturn(examTimeInfo);
        when(service.getProfessorByFachId(profFachId)).thenReturn(p);

        when(service.getAttempt(examFachId, "Marvin0109")).thenReturn(versuchDTO);

        mvc.perform(get("/exams/examsDurchfuehren/{examFachId}/menu", examFachId))
            .andExpect(status().isOk())
            .andExpect(view().name("exams/examMenu"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attribute("timeLeft", "Anzeige"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", true))
            .andExpect(model().attribute("attempt", versuchDTO))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    @DisplayName("Der Zugang zum Exam ist nicht erlaubt ohne Anmeldung")
    void testExamAccess() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/exams/examsDurchfuehren/{examFachId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Starten des Exams erfolgreich")
    void startExams_01() throws Exception {
        UUID examFachId = UUID.randomUUID();
        ExamForm form = mock(ExamForm.class);

        when(service.fillExamForm(examFachId)).thenReturn(form);

        mvc.perform(get("/exams/examsDurchfuehren/{examFachId}", examFachId))
            .andExpect(status().isOk())
            .andExpect(model().attribute("exam", form))
            .andExpect(model().attributeExists("submitForm"))
            .andExpect(view().name("exams/examsDurchfuehren"));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams nicht erfolgreich (egal ob Exam vorher eingereicht oder nicht)")
    void submitExam_02() throws Exception {
        UUID examFachId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q1", List.of("A"),
                "q2", List.of("B", "C")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        when(service.examIsAlreadySubmitted(examFachId, "Marvin0109")).thenReturn(false);
        when(service.submitExam(eq("Marvin0109"), any(), eq(examFachId))).thenReturn(false);

        mvc.perform(post("/exams/submit/{examFachId}", examFachId)
                .flashAttr("antworten", form)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/exams/examsStudierende"))
            .andExpect(flash().attribute("message", "Fehler beim Einreichen der Antworten."))
            .andExpect(flash().attribute("success", false));

        verify(service).examIsAlreadySubmitted(examFachId, "Marvin0109");
        verify(service).submitExam(eq("Marvin0109"), any(), eq(examFachId));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams ist erfolgreich (noch kein Exam vorher eingereicht)")
    void submitExam_03() throws Exception {
        UUID examFachId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q1", List.of("A"),
                "q2", List.of("B", "C")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        when(service.examIsAlreadySubmitted(examFachId, "Marvin0109")).thenReturn(false);
        when(service.submitExam(eq("Marvin0109"), any(), eq(examFachId))).thenReturn(true);

        mvc.perform(post("/exams/submit/{examFachId}", examFachId)
                        .flashAttr("antworten", form)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exams/examsStudierende"))
                .andExpect(flash().attribute("message", "Alle Antworten erfolgreich eingereicht!"))
                .andExpect(flash().attribute("success", true));

        verify(service).examIsAlreadySubmitted(examFachId, "Marvin0109");
        verify(service).submitExam(eq("Marvin0109"), any(), eq(examFachId));
    }

    @Test
    @WithMockOAuth2User(login = "Marvin0109", roles = {"STUDENT"})
    @DisplayName("Das einreichen des Exams ist erfolgreich (mit Eingabedaten im richtigen Format)")
    void submitExam_04() throws Exception {
        UUID examFachId = UUID.randomUUID();
        UUID frageId1 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        UUID frageId3 = UUID.randomUUID();

        when(service.examIsAlreadySubmitted(examFachId, "Marvin0109")).thenReturn(false);
        when(service.submitExam(eq("Marvin0109"), any(), eq(examFachId))).thenReturn(true);

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