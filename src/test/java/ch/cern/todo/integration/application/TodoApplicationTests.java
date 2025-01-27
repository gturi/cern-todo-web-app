package ch.cern.todo.integration.application;

import ch.cern.todo.integration.config.ContainerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContainerTest
class TodoApplicationTests {

	@Test
	void contextLoads() {
        log.info("Application started successfully");
	}

}
