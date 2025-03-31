package exambyte.application;

public class TestSystemPropertyInitializer {

    // TODO: Entweder hier die Umgebungsvariablen setzen für tests oder in build.gradle
    public static void init() {
        System.setProperty("CLIENT_ID", "test-client-id");
        System.setProperty("CLIENT_SECRET", "test-client-secret");
        System.setProperty("DB_NAME", "test-db");
        System.setProperty("DB_USERNAME", "test-user");
        System.setProperty("DB_PASSWORD", "test-password");
    }
}
