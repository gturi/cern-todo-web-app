package ch.cern.todo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled // TODO: check why liquibase tries to create the database schema even if it is already created, probably it would be best to use test containers
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoApplicationTests {

	@Test
	void contextLoads() {
        log.info("Application started successfully");
	}

}
