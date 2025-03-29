package exambyte.application;

import exambyte.infrastructure.persistence.container.TestcontainerConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
public class ExamByteApplicationTests {

	@BeforeAll
	static void setup() {
		TestSystemPropertyInitializer.init();
	}

	@Test
	void contextLoads() {
		assertNotNull(System.getProperty("CLIENT_ID"));
		assertEquals("test-client-id", System.getProperty("CLIENT_ID"));
	}

}
