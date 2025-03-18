package exambyte;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class SystemPropertyInitializer {

    public static void init() {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("CLIENT_ID", Objects.requireNonNull(dotenv.get("CLIENT_ID")));
        System.setProperty("CLIENT_SECRET", Objects.requireNonNull(dotenv.get("CLIENT_SECRET")));
        System.setProperty("DB_NAME", Objects.requireNonNull(dotenv.get("DB_NAME")));
        System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
    }
}
