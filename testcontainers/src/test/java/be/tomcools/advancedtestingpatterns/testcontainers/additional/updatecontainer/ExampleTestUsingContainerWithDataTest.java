package be.tomcools.advancedtestingpatterns.testcontainers.additional.updatecontainer;

import be.tomcools.advancedtestingpatterns.app.velo.VeloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExampleTestUsingContainerWithDataTest {

    // This is needed to use the PostgreSQLContainer class ;)
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName
            .parse(LiquibaseContainerUpdaterTest.FULL_IMAGE_NAME)
            .asCompatibleSubstituteFor("postgres");

    @Container
    private static final PostgreSQLContainer<?> SQL_CONTAINER = populateDatabase();

    @Autowired
    private VeloRepository repo;

    @Test
    void testIfDataSavedInUpdaterIsActuallyPresent() {
        // we expect there to be 1 because we added it inside the tomcools/postgres image created by LiquibaseContainerUpdater.class
        assertThat(repo.count()).isEqualTo(1);
    }

    @DynamicPropertySource
    private static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", SQL_CONTAINER::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
    }

    private static PostgreSQLContainer<?> populateDatabase() {
        try (PostgreSQLContainer<?> db = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME)) {
            // map container data to "non volume path" as only those are saved on commit.
            // original volume path is "/var/lib/postgresql/data"
            return db.withImagePullPolicy(PullPolicy.alwaysPull())
                    // Custom waiter, because of https://github.com/testcontainers/testcontainers-java/issues/5359
                    .waitingFor((new LogMessageWaitStrategy())
                            .withRegEx(".*database system is ready to accept connections.*\\s")
                            .withTimes(1)
                            .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
                    )
                    .withDatabaseName("testcontainer")
                    .withUsername("sa")
                    .withPassword("sa");
        }
    }
}
