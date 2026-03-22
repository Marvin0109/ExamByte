package exambyte.web.controllers;

import exambyte.application.dto.ExamDTO;
import exambyte.application.dto.ProfessorDTO;
import exambyte.application.dto.AttemptDTO;
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
    void get_listExamsForStudents_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/examListForStudent"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_listExamsForStudents_authorized() throws Exception {
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
    void get_examMenu_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/startExam/{examId}/menu", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_examMenu_success_examNotSubmittedYet() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Display", true);
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
            .andExpect(model().attribute("timeLeft", "Display"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", false))
            .andExpect(model().attributeExists("reviewPermission"))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    @DisplayName("Prüfungsmenü ist erreichbar (Exam eingereicht vorher)")
    void get_examMenu_success_examSubmittedYet() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID profId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);

        ExamTimeInfo examTimeInfo = new ExamTimeInfo("Display", true);
        ExamDTO examDTO = new ExamDTO(examId, "Exam 1", profId,
                start, start.plusHours(1), start.plusHours(2));
        ProfessorDTO professor = new ProfessorDTO(profId, "ProfName");
        AttemptDTO attemptDTO = mock(AttemptDTO.class);

        when(service.getExamByUUID(examId)).thenReturn(examDTO);
        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(true);
        when(service.getExamTimeInfo(examDTO)).thenReturn(examTimeInfo);
        when(service.getProfessorById(profId)).thenReturn(professor);

        when(service.getAttempt(examId, "username")).thenReturn(attemptDTO);

        mvc.perform(get("/student/startExam/{examId}/menu", examId))
            .andExpect(status().isOk())
            .andExpect(view().name("student/examMenu"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attribute("timeLeft", "Display"))
            .andExpect(model().attribute("timeLeftBool", true))
            .andExpect(model().attribute("alreadySubmitted", true))
            .andExpect(model().attribute("attempt", attemptDTO))
            .andExpect(model().attributeExists("reviewPermission"))
            .andExpect(model().attribute("authorName", "ProfName"));
    }

    @Test
    void get_startExam_notAuthorized() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/student/startExam/{examId}", UUID.randomUUID()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
        assertThat(mvcResult.getResponse().getRedirectedUrl())
            .contains("oauth2/authorization/github");
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_startExam_authorized() throws Exception {
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
    void post_submitExam_fail() throws Exception {
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
    void post_submitExam_fail_answerMissing() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of() // Null
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
    void post_submitExam_fail_answerMissing_2() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of("") // Empty
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
    void post_submitExam_fail_answerMissing_3() throws Exception {
        UUID examId = UUID.randomUUID();

        Map<String, List<String>> answers = Map.of(
                "q2", List.of(" ") // Blank
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
    void post_submitExam_success_noSubmitsBefore() throws Exception {
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
    void post_submitExam_success_inputDataInCorrectFormat() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID questionId1 = UUID.randomUUID();
        UUID questionId2 = UUID.randomUUID();
        UUID questionId3 = UUID.randomUUID();

        when(service.examIsAlreadySubmitted(examId, "username")).thenReturn(false);
        when(service.submitExam(eq("username"), any(), eq(examId))).thenReturn(true);

        mvc.perform(post("/student/submit/{examId}", examId)
                .with(csrf())
                .param("answers[" + questionId1 + "]", "Answer 1") // SC
                .param("answers[" + questionId2 + "]", "Answer 1", "Answer 2") // MC
                .param("answers[" + questionId3 + "]", "Long answer"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("message", "Alle Antworten erfolgreich eingereicht!"))
            .andExpect(flash().attribute("success", true))
            .andExpect(redirectedUrl("/student/examListForStudent"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_showReview_success() throws Exception {
        when(service.prepareReviewViewForm(any(), any())).thenReturn(mock());
        when(service.checkTimeForReviewView(any())).thenReturn(true);

        mvc.perform(get("/student/showReview/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(view().name("student/showReview"))
                .andExpect(model().attributeExists("view"));
    }

    @Test
    @WithMockOAuth2User(roles = {"STUDENT"})
    void get_showReview_fail() throws Exception {
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
    void get_startWithData_success() throws Exception {
        when(service.fillOldDataForm(any(), any())).thenReturn(mock());
        when(service.fillSubmitFormWithData(any())).thenReturn(mock());

        mvc.perform(get("/student/startWithData/{examId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exam"))
                .andExpect(model().attributeExists("submitForm"))
                .andExpect(view().name("student/startExamWithData"));
    }
}