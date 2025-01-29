package exambyte.application;

import exambyte.persistence.container.TestcontainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
class ExamByteApplicationTests {

	@Test
	void contextLoads() {
	}

}
