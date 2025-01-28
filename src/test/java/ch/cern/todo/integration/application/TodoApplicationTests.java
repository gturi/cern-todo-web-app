package ch.cern.todo.integration.application;

import ch.cern.todo.integration.config.ContainerTest;
import ch.cern.todo.integration.config.SpringBootIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootIntegrationTest
@ContainerTest
class TodoApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        val activeProfiles = Arrays.stream(environment.getActiveProfiles()).toList();
        log.info("Application started successfully, active profiles: {}", activeProfiles);
        assertTrue(activeProfiles.contains("test"));
    }

}
