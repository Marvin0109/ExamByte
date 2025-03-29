import exambyte.ExamByteApplication;
import exambyte.application.TestSystemPropertyInitializer;
import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import org.springframework.boot.SpringApplication;

public class ExamByteWithContainer {

    public static void main(String[] args) {
        TestSystemPropertyInitializer.init();
        SpringApplication.from(ExamByteApplication::main)
                 .with(TestcontainerConfiguration.class)
                .run(args);
    }
}
