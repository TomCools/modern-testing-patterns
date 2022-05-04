package be.tomcools.advancedtestingpatterns.testcontainers.additional;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PushResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LiquibaseContainerUpdater {

    /* NOTE
    A blog post about this solution is in the works... not going to comment everything here in the meantime ;)
     */

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2")
            .withImagePullPolicy(PullPolicy.alwaysPull())
            // Custom waiter
            .waitingFor((new LogMessageWaitStrategy())
                    .withRegEx(".*database system is ready to accept connections.*\\s")
                    .withTimes(1)
                    .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS))
            )
            // map container data to "non volume path" as only those are saved on commit.
            .withEnv("PGDATA", "/var/lib/postgresql/container-data")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    @Test
    @Disabled("Should be run only with certain profiles ;)")
    public void pushNewImage() throws InterruptedException {
        // Startup and Liquibase happen first :-)

        // Commit docker container changes into new image
        final DockerClient dockerClient = postgreSQLContainer.getDockerClient();
        dockerClient.commitCmd(postgreSQLContainer.getContainerId())
                .withRepository("tomcools/postgres")
                .withTag("dev")
                .exec();

        // Push new image
        dockerClient.pushImageCmd("tomcools/postgres:dev")
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(PushResponseItem object) {
                        //log.info(object.toString());
                    }
                }).awaitCompletion();
    }

    @DynamicPropertySource
    static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.liquibase.enabled", () -> true);
    }
}
