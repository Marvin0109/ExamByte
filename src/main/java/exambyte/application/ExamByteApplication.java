package exambyte.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.Desktop; import java.io.IOException; import java.net.URI; import java.net.URISyntaxException;

/**
 * Die Hauptklasse der Anwendung, die den Spring Boot Server startet und eine URL in einem Browser öffnet. ඞ
 *
 * Diese Klasse enthält die 'main'-Methode, die den Spring Boot-Anwendung-Context startet. Zusätzlich wird beim Starten der Anwendung
 * eine vordefinierte URL xD in einem bevorzugten Browser geöffnet.
 *
 * Wenn der Desktop nicht unterstützt wird, wird versucht, die die URL über einen der gängigen
 * Browser (z. B. Firefox, Chrome, Edge) zu öffnen. Falls auch dieser Schritt fehlschlägt, wird eine Fehlermeldung ausgegeben.
 *
 * @see SpringApplication
 */

@SpringBootApplication(scanBasePackages = {"exambyte.*", "resources.*", "test.*"})
public class ExamByteApplication {

	/**
	 * Versucht, eine URL im Standardbrowser zu öffnen.
	 * Wenn der Desktop nicht unterstützt wird, wird die URL in einem der gängigen Browser (Firefox, Chrome, Edge) geöffnet.
	 * Falls alle Versuche fehlschlagen, wird eine Fehlermeldung ausgegeben.
	 *
	 * @param uri Die URI, die im Browser geöffnet werden soll.
	 */
	private static void openInBrowser(URI uri) {
		String[] browsers = {"firefox", "google-chrome", "microsoft-edge"};
		for (String browser : browsers) {
			try {
				Runtime.getRuntime().exec(browser + " " + uri.toString());
				return;
			} catch (IOException e) {
				// Fehlerbehandlung für den Fall, dass der Browser nicht geöffnet werden konnte
			}
		}
		System.err.println("Keiner der Browser konnte geöffnet werden.");
	}

	/**
	 * Die main-Methode, die den Spring Boot-Anwendungsserver startet und eine URL in einem Browser öffnet.
	 *
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		// Starten der Spring Boot-Anwendung
		SpringApplication.run(ExamByteApplication.class, args);
		try {
			// Die URL, die beim Starten der Anwendung geöffnet werden soll
			URI uri = new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ");

			// Überprüfen, ob der Desktop zum Öffnen von URLs unterstützt wird
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			} else {
				openInBrowser(uri);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace(); // Fehlerprotokollierung
		}
	}
}