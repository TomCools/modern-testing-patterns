package be.tomcools.moderntestingpatterns.testcontainers.additional;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcServiceConnection;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = ModernTestingPatternsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.liquibase.enabled=true"})
public class SpringServiceConnectionExample {

    // This will only be available in Spring Boot 3.1!
    @Container
    @JdbcServiceConnection
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    @Test
    public void init() {
        // context started
    }
}
