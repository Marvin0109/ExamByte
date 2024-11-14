package exambyte.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.Desktop; import java.io.IOException; import java.net.URI; import java.net.URISyntaxException;


@SpringBootApplication
public class ExamByteApplication {

	private static void openInBrowser(URI uri) {
		String[] browsers = {"firefox", "google-chrome", "microsoft-edge"};
		for (String browser : browsers) {
			try {
				Runtime.getRuntime().exec(browser + " " + uri.toString());
				return;
			} catch (IOException e) {
			}
		}
		System.err.println("Keiner der Browser konnte geöffnet werden.");
	}

	public static void main(String[] args) {
		SpringApplication.run(ExamByteApplication.class, args);
		try {
			URI uri = new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			} else {
				openInBrowser(uri);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}