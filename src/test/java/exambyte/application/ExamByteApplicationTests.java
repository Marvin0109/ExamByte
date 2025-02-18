package exambyte.application;

import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
public class ExamByteApplicationTests {

	@Test
	void contextLoads() {
	}

}
