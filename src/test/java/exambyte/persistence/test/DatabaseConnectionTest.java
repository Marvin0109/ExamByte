package exambyte.persistence.test;

import exambyte.application.ExamByteApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Der Test prüft, ob Spring mit der Datenbank kommunizieren kann.
 */
@SpringBootTest(classes = ExamByteApplication.class)
@Transactional
public class DatabaseConnectionTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testDatabaseConnection() {
        // Test, ob die EntityManagerFactory vorhanden ist
        assertThat(entityManagerFactory).isNotNull();

        // Test, ob der EntityManager eine Verbindung zur Datenbank herstellen kann
        assertThat(entityManager).isNotNull();

        // Versuche eine einfache Datenbankabfrage (z.B. eine Entität abzurufen oder zu speichern)
        // Dies stellt sicher, dass die Verbindung und Kommunikation mit der Datenbank funktioniert
        Long count = (Long) entityManager.createQuery("SELECT COUNT(e) FROM MyEntity e")
                .getSingleResult();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}
