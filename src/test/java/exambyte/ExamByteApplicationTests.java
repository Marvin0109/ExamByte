package exambyte;

import exambyte.infrastructure.container.TestcontainerConfiguration;
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
