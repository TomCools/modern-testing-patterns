package be.tomcools.advancedtestingpatterns.testcontainers.additional.updatecontainer;

import be.tomcools.advancedtestingpatterns.app.velo.VeloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExampleTestUsingContainerWithData {

    // This is needed to use the PostgreSQLContainer class ;)
    private static DockerImageName IMAGE = DockerImageName.parse("tomcools/postgres:main")
            .asCompatibleSubstituteFor("postgres");

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(IMAGE)
            .withImagePullPolicy(PullPolicy.alwaysPull())
            // Custom waiter, because of https://github.com/testcontainers/testcontainers-java/issues/5359
            .waitingFor((new LogMessageWaitStrategy())
                    .withRegEx(".*database system is ready to accept connections.*\\s")
                    .withTimes(1)
                    .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
            )
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");;


    @Autowired
    VeloRepository repo;

    @Test
    public void testIfDataSavedInUpdaterIsActuallyPresent() {
        // we expect there to be 1 because we added it inside the tomcools/postgres image created by LiquibaseContainerUpdater.class
        assertThat(repo.count()).isEqualTo(1);
    }

    @DynamicPropertySource
    static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.liquibase.enabled", () -> true);
    }
}
