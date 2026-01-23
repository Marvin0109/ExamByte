package exambyte.domain.aggregate.impl;

import exambyte.domain.model.aggregate.exam.Antwort;
import exambyte.domain.model.aggregate.exam.Exam;
import exambyte.domain.model.aggregate.exam.Frage;
import exambyte.domain.model.aggregate.exam.Review;
import exambyte.domain.model.common.QuestionType;
import exambyte.domain.model.impl.ZulassungService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ZulassungServiceTest {

    @Test
    @DisplayName("berechneErreichtePunkte Test (angenommen, MC und SC Bewertung sei korrekt implementiert worden")
    void test_01() {
        // Arrange
        ZulassungService zulassungService = new ZulassungService();

        UUID studentId = UUID.randomUUID();
        UUID profFachID = UUID.randomUUID();

        UUID examId1 = UUID.randomUUID();
        UUID examId2 = UUID.randomUUID();

        UUID frage1FachID = UUID.randomUUID();
        UUID frage2FachID = UUID.randomUUID();
        UUID frage3FachID = UUID.randomUUID();

        UUID review1ID = UUID.randomUUID();
        UUID review2ID = UUID.randomUUID();
        UUID review3ID = UUID.randomUUID();

        UUID antwort1ID = UUID.randomUUID();
        UUID antwort2ID = UUID.randomUUID();
        UUID antwort3ID = UUID.randomUUID();

        LocalDateTime start1 = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result1 = LocalDateTime.of(2020, 1, 1, 2, 0);

        LocalDateTime start2 = LocalDateTime.of(2020, 1, 1, 3, 0);
        LocalDateTime end2 = LocalDateTime.of(2020, 1, 1, 4, 0);
        LocalDateTime result2 = LocalDateTime.of(2020, 1, 1, 5, 0);

        Exam exam1 = new Exam.ExamBuilder()
                .fachId(examId1)
                .title("Exam 1")
                .professorFachId(profFachID)
                .startTime(start1)
                .endTime(end1)
                .resultTime(result1)
                .build();

        Exam exam2 = new Exam.ExamBuilder()
                .fachId(examId2)
                .title("Exam 2")
                .professorFachId(profFachID)
                .startTime(start2)
                .endTime(end2)
                .resultTime(result2)
                .build();

        Frage frage1 = new Frage.FrageBuilder()
                .fachId(frage1FachID)
                .frageText("Frage 1")
                .maxPunkte(10)
                .professorUUID(profFachID)
                .examUUID(exam1.getFachId())
                .type(QuestionType.FREITEXT)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(frage2FachID)
                .frageText("Frage 2")
                .maxPunkte(5)
                .professorUUID(profFachID)
                .examUUID(exam1.getFachId())
                .type(QuestionType.MC)
                .build();

        Frage frage3 = new Frage.FrageBuilder()
                .fachId(frage3FachID)
                .frageText("Frage 1")
                .maxPunkte(8)
                .professorUUID(profFachID)
                .examUUID(exam2.getFachId())
                .type(QuestionType.SC)
                .build();

        Antwort antwort1 = new Antwort.AntwortBuilder()
                .fachId(antwort1ID)
                .antwortText("Meine Antwort")
                .frageFachId(frage1FachID)
                .studentFachId(studentId)
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        Antwort antwort2 = new Antwort.AntwortBuilder()
                .fachId(antwort2ID)
                .antwortText("Meine Antwort 2")
                .frageFachId(frage2FachID)
                .studentFachId(studentId)
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        Antwort antwort3 = new Antwort.AntwortBuilder()
                .fachId(antwort3ID)
                .antwortText("Meine Antwort 3")
                .frageFachId(frage3FachID)
                .studentFachId(studentId)
                .antwortZeitpunkt(LocalDateTime.now())
                .lastChangesZeitpunkt(LocalDateTime.now())
                .build();

        Review review1 = new Review.ReviewBuilder()
                .fachId(review1ID)
                .antwortFachId(antwort1ID)
                .korrektorFachId(UUID.randomUUID())
                .bewertung("schlecht")
                .punkte(4)
                .build();

        Review review2 = new Review.ReviewBuilder()
                .fachId(review2ID)
                .antwortFachId(antwort2ID)
                .korrektorFachId(UUID.randomUUID())
                .bewertung("gut")
                .punkte(4)
                .build();

        Review review3 = new Review.ReviewBuilder()
                .fachId(review3ID)
                .antwortFachId(antwort3ID)
                .korrektorFachId(UUID.randomUUID())
                .bewertung("sehr gut")
                .punkte(8)
                .build();

        List<Exam> exams = List.of(exam1, exam2); // Exam1: Frage 1, Frage 2; Exam2: Frage 3
        List<Frage> fragen = List.of(frage1, frage2, frage3);
        List<Antwort> antworten = List.of(antwort1, antwort2, antwort3);
        List<Review> bewertungen = List.of(review1, review2, review3);

        // Act
        List<Integer> result = zulassungService.berechneErreichtePunkte(exams, fragen, bewertungen, antworten, studentId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.getFirst()).isEqualTo(8); // review1 + review2 = 4 + 4 = 8
        assertThat(result.getLast()).isEqualTo(8); // review3
    }

    @Test
    @DisplayName("Zulassung Test (keine Zulassung erhalten); Zulassung erst ab 50% der Punkte pro Test")
    void test_02() {
        // Arrange
        ZulassungService zulassungService = new ZulassungService();

        List<Integer> punkteProExam = List.of(10, 12, 14);
        List<Integer> erreichtePunkte = List.of(3, 6, 9);

        // Act
        boolean result = zulassungService.berechneZulassung(punkteProExam, erreichtePunkte);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Zulassung Test (Zulassung erhalten)")
    void test_03() {
        // Arrange
        ZulassungService zulassungService = new ZulassungService();

        List<Integer> punkteProExam = List.of(10, 12, 14);
        List<Integer> erreichtePunkte = List.of(5, 7, 9);

        // Act
        boolean result = zulassungService.berechneZulassung(punkteProExam, erreichtePunkte);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("berechneTestPunkte")
    void test_04() {
        // Arrange
        ZulassungService zulassungService = new ZulassungService();

        UUID profFachID = UUID.randomUUID();

        UUID examId1 = UUID.randomUUID();
        UUID examId2 = UUID.randomUUID();

        UUID frage1FachID = UUID.randomUUID();
        UUID frage2FachID = UUID.randomUUID();
        UUID frage3FachID = UUID.randomUUID();

        LocalDateTime start1 = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2020, 1, 1, 1, 0);
        LocalDateTime result1 = LocalDateTime.of(2020, 1, 1, 2, 0);

        LocalDateTime start2 = LocalDateTime.of(2020, 1, 1, 3, 0);
        LocalDateTime end2 = LocalDateTime.of(2020, 1, 1, 4, 0);
        LocalDateTime result2 = LocalDateTime.of(2020, 1, 1, 5, 0);

        Exam exam1 = new Exam.ExamBuilder()
                .fachId(examId1)
                .title("Exam 1")
                .professorFachId(profFachID)
                .startTime(start1)
                .endTime(end1)
                .resultTime(result1)
                .build();

        Exam exam2 = new Exam.ExamBuilder()
                .fachId(examId2)
                .title("Exam 2")
                .professorFachId(profFachID)
                .startTime(start2)
                .endTime(end2)
                .resultTime(result2)
                .build();

        Frage frage1 = new Frage.FrageBuilder()
                .fachId(frage1FachID)
                .frageText("Frage 1")
                .maxPunkte(10)
                .professorUUID(profFachID)
                .examUUID(exam1.getFachId())
                .type(QuestionType.FREITEXT)
                .build();

        Frage frage2 = new Frage.FrageBuilder()
                .fachId(frage2FachID)
                .frageText("Frage 2")
                .maxPunkte(5)
                .professorUUID(profFachID)
                .examUUID(exam1.getFachId())
                .type(QuestionType.FREITEXT)
                .build();

        Frage frage3 = new Frage.FrageBuilder()
                .fachId(frage3FachID)
                .frageText("Frage 1")
                .maxPunkte(8)
                .professorUUID(profFachID)
                .examUUID(exam2.getFachId())
                .type(QuestionType.FREITEXT)
                .build();

        List<Exam> exams = List.of(exam1, exam2);
        List<Frage> fragen = List.of(frage1, frage2, frage3);

        // Act
        List<Integer> punkteProExam = zulassungService.berechneTestPunkte(exams, fragen);

        // Assert
        assertThat(punkteProExam).hasSize(2);
        assertThat(punkteProExam.getFirst()).isEqualTo(15);
        assertThat(punkteProExam.getLast()).isEqualTo(8);
    }
}
