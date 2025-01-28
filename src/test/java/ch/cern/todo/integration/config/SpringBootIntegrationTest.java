package ch.cern.todo.integration.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public @interface SpringBootIntegrationTest {
    @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles") String[] activeProfiles() default {"test"};
}
