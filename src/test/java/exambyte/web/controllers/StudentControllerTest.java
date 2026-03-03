package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.VersuchDTO;
import exambyte.infrastructure.config.MethodSecurityConfig;
import exambyte.infrastructure.config.SecurityConfig;
import exambyte.application.service.AppUserService;
import exambyte.web.controllers.securityHelper.WithMockOAuth2User;
import exambyte.web.form.create_exam.ExamForm;
import exambyte.web.form.info.ExamTimeInfo;
import exambyte.web.form.submit_answers.SubmitForm;
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

@WebMvcTest(StudentController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private ExamControllerService service;

    @Test
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für nicht authentifizierte User nicht erreichbar")
    void listExamsForStudents_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/examListForStudent"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Die Seite zum Ansehen von Prüfungen ist für Studierende sichtbar")
    void listExamsForStudents_02() throws Exception {
        mvc.perform(get("/student/examListForStudent"))
            .andExpect(status().isOk())
            .andExpect(view().name("student/examListForStudent"))
            .andExpect(model().attribute("name", "username"))
            .andExpect(model().attributeExists("currentPath"))
            .andExpect(model().attributeExists("exams"))
            .andExpect(model().attributeExists("timeNow"))
            .andExpect(model().attributeExists("progress"))
            .andExpect(model().attributeExists("failedYetOrNot"));
    }

    @Test
    @DisplayName("Prüfungsmenü nicht erreichbar für nicht authentifizierte User")
    void examMenu_01() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/startExam/{examId}/menu", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Prüfungsmenü ist erreichbar (noch kein Exam eingereicht vorher)")
    void examMenu_02() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Anzeige", true);
        ExamDTO examDTO = new ExamDTO(examId, "Exam 1", profId,
                start, start.plusHours(1), start.plusHours(2));
        ProfessorDTO p = new ProfessorDTO(profId, "ProfName");

        when(service.getExamByUUID(examId)).thenReturn(examDTO);
        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(false);
        when(service.getExamTimeInfo(examDTO)).thenReturn(examTimeInfo);
        when(service.getProfessorById(profId)).thenReturn(p);

        mvc.perform(get("/student/startExam/{examId}/menu", examId))
            .andExpect(status().isOk())
            .andExpect(view().name("student/examMenu"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attribute("timeLeft", "Anzeige"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", false))
            .andExpect(model().attributeExists("reviewPermission"))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Prüfungsmenü ist erreichbar (Exam eingereicht vorher)")
    void examMenu_03() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Anzeige", true);
        ExamDTO examDTO = new ExamDTO(examId, "Exam 1", profId,
                start, start.plusHours(1), start.plusHours(2));
        ProfessorDTO p = new ProfessorDTO(profId, "ProfName");
        VersuchDTO versuchDTO = mock(VersuchDTO.class);

        when(service.getExamByUUID(examId)).thenReturn(examDTO);
        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(true);
        when(service.getExamTimeInfo(examDTO)).thenReturn(examTimeInfo);
        when(service.getProfessorById(profId)).thenReturn(p);

        when(service.getAttempt(examId, "username")).thenReturn(versuchDTO);

        mvc.perform(get("/student/startExam/{examId}/menu", examId))
            .andExpect(status().isOk())
            .andExpect(view().name("student/examMenu"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attribute("timeLeft", "Anzeige"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", true))
            .andExpect(model().attribute("attempt", versuchDTO))
            .andExpect(model().attributeExists("reviewPermission"))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    @DisplayName("Der Zugang zum Exam ist nicht erlaubt ohne Anmeldung")
    void testExamAccess() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/startExam/{examId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Starten des Exams erfolgreich")
    void startExams_01() throws Exception {
        UUID examId = UUID.randomUUID();
        ExamForm form = mock(ExamForm.class);

        when(service.fillExamForm(examId)).thenReturn(form);

        mvc.perform(get("/student/startExam/{examId}", examId))
            .andExpect(status().isOk())
            .andExpect(model().attribute("exam", form))
            .andExpect(model().attributeExists("submitForm"))
            .andExpect(view().name("student/startExam"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams nicht erfolgreich (egal ob Exam vorher eingereicht oder nicht)")
    void submitExam_02() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q1", List.of("A"),
                "q2", List.of("B", "C")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(false);
        when(service.submitExam(eq("username"), any(), eq(examId))).thenReturn(false);

        mvc.perform(post("/student/submit/{examId}", examId)
                .flashAttr("submitForm", form)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/student/examListForStudent"))
            .andExpect(flash().attribute("message", "Fehler beim Einreichen der Antworten."))
            .andExpect(flash().attribute("success", false));

        verify(service).submitExam(eq("username"), any(), eq(examId));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams nicht erfolgreich (fehlende Antwort)")
    void submitExam_03() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of()
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        mvc.perform(post("/student/submit/{examId}", examId)
                        .flashAttr("submitForm", form)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/examListForStudent"))
                .andExpect(flash().attribute("message", "Alle Antworten müssen gesetzt werden!"))
                .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams nicht erfolgreich (fehlende Antwort)")
    void submitExam_04() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of("")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        mvc.perform(post("/student/submit/{examId}", examId)
                        .flashAttr("submitForm", form)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/examListForStudent"))
                .andExpect(flash().attribute("message", "Alle Antworten müssen gesetzt werden!"))
                .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams nicht erfolgreich (fehlende Antwort)")
    void submitExam_05() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of(" ")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        mvc.perform(post("/student/submit/{examId}", examId)
                        .flashAttr("submitForm", form)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/examListForStudent"))
                .andExpect(flash().attribute("message", "Alle Antworten müssen gesetzt werden!"))
                .andExpect(flash().attribute("success", false));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das Einreichen eines Exams ist erfolgreich (noch kein Exam vorher eingereicht)")
    void submitExam_06() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q1", List.of("A"),
                "q2", List.of("B", "C")
        );

        SubmitForm form = new SubmitForm();
        form.setAnswers(answers);

        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(false);
        when(service.submitExam(eq("username"), any(), eq(examId))).thenReturn(true);

        mvc.perform(post("/student/submit/{examId}", examId)
                        .flashAttr("submitForm", form)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/examListForStudent"))
                .andExpect(flash().attribute("message", "Alle Antworten erfolgreich eingereicht!"))
                .andExpect(flash().attribute("success", true));

        verify(service).submitExam(eq("username"), any(), eq(examId));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Das einreichen des Exams ist erfolgreich (mit Eingabedaten im richtigen Format)")
    void submitExam_07() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID frageId1 = UUID.randomUUID();
        UUID frageId2 = UUID.randomUUID();
        UUID frageId3 = UUID.randomUUID();

        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(false);
        when(service.submitExam(eq("username"), any(), eq(examId))).thenReturn(true);

        mvc.perform(post("/student/submit/{examId}", examId)
                .with(csrf())
                .param("answers[" + frageId1 + "]", "Antwort 1") // SC
                .param("answers[" + frageId2 + "]", "Antwort 1", "Antwort 2") // MC
                .param("answers[" + frageId3 + "]", "Dies ist meine Freitext Antwort"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Alle Antworten erfolgreich eingereicht!"))
            .andExpect(flash().attribute("success", true))
            .andExpect(redirectedUrl("/student/examListForStudent"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Die Korrektureinsicht ist erfolgreich")
    void showReview() throws Exception {
        when(service.prepareReviewViewForm(any(), any())).thenReturn(mock());
        when(service.checkTimeForReviewView(any())).thenReturn(true);

        mvc.perform(get("/student/showReview/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(view().name("student/showReview"))
                .andExpect(model().attributeExists("view"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Korrektureinsicht nicht erfolgreich, da Ergebniszeit noch nicht erreicht wurde")
    void showReview_failure() throws Exception {
        when(service.prepareReviewViewForm(any(), any())).thenReturn(mock());
        when(service.checkTimeForReviewView(any())).thenReturn(false);

        mvc.perform(get("/student/showReview/{examId}", UUID.randomUUID()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/examListForStudent"))
                .andExpect(flash().attribute("success", false))
                .andExpect(flash().attribute("message", "Korrektureinsicht noch nicht verfügbar!"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Testversuch mit alten Antworten ist erfolgreich")
    void startWithData() throws Exception {
        when(service.fillOldDataForm(any(), any())).thenReturn(mock());
        when(service.fillSubmitFormWithData(any())).thenReturn(mock());

        mvc.perform(get("/student/startWithData/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exam"))
                .andExpect(model().attributeExists("submitForm"))
                .andExpect(view().name("student/startExamWithData"));
    }
}