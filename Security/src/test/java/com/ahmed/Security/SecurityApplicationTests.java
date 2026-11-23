package com.ahmed.Security;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.SpringBootTest;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SecurityApplicationTests {

	@Test
	void contextLoads() {
	}
}