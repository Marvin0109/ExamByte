package exambyte;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;
import java.util.logging.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * The main class of the application that starts the Spring Boot server and opens a URL in a browser. ඞ
 * This class contains the 'main' method, which starts the Spring Boot application context.
 * Additionally, when the application starts, the predefined URL localhost:8080 is opened in the preferred
 * browser.
 * If the desktop environment is not supported, the application attempts to open the URL using one of the
 * common browsers (e.g., Firefox, Chrome, Edge).
 * If this step also fails, an error message is displayed.
 *
 * @see SpringApplication
 */

@SpringBootApplication()
public class ExamByteApplication {

	private static final Logger logger = Logger.getLogger(ExamByteApplication.class.getName());
	private static final URI LOCALHOST_URI = URI.create("http://localhost:8080");

	private static void init() {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("CLIENT_ID", Objects.requireNonNull(dotenv.get("CLIENT_ID")));
		System.setProperty("CLIENT_SECRET", Objects.requireNonNull(dotenv.get("CLIENT_SECRET")));
		System.setProperty("DB_NAME", Objects.requireNonNull(dotenv.get("DB_NAME")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
	}

	private static void openInBrowser(URI uri) {
		String[] browsers = {"firefox", "google-chrome", "microsoft-edge"};

		for (String browser : browsers) {
			try {
				new ProcessBuilder(browser, uri.toString()).start();
				return;
			} catch (IOException ignored) {
				// Browser not available -> next
			}
		}

		logger.info("Non of the supported browsers are found.");
	}

	public static void main(String[] args) {
		init();

		SpringApplication.run(ExamByteApplication.class, args);
		try {
			openInBrowser(LOCALHOST_URI);
		} catch (Exception e) {
			logger.info("Invalid URI: " + e.getMessage());
		}
	}
}