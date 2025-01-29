import exambyte.application.ExamByteApplication;
import exambyte.persistence.container.TestcontainerConfiguration;
import org.springframework.boot.SpringApplication;

public class ExamByteWithContainer {

    public static void main(String[] args) {
        SpringApplication.from(ExamByteApplication::main)
                 .with(TestcontainerConfiguration.class)
                .run(args);
    }
}
