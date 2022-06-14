package be.tomcools.advancedtestingpatterns.testcontainers.additional.updatecontainer;

import be.tomcools.advancedtestingpatterns.app.velo.VeloRepository;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushResponseItem;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

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
            // map container data to "non volume path" as only those are saved on commit.
            // original volume path is "/var/lib/postgresql/data"
            .withEnv("PGDATA", "/var/lib/postgresql/container-data")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    VeloRepository repo;

    @Test
    @Disabled("Should be run only with certain profiles ;)")
    public void pushNewImage() throws InterruptedException {
        // Startup and Liquibase happen first :-)

        // You can even use some code to add data!
        repo.save(VeloStation.builder().id("X").name("PRE_PROVISIONED").build());

        // Commit docker container changes into new image
        final DockerClient dockerClient = postgreSQLContainer.getDockerClient();
        final String result = dockerClient.commitCmd(postgreSQLContainer.getContainerId())
                .withRepository("tomcools/postgres")
                .withTag("main")
                .exec();

        // Push new image
        dockerClient.pushImageCmd("tomcools/postgres:main")
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(PushResponseItem object) {
                        // This is just to fail the build in case push of new image fails
                        log.info(object.toString());
                        if(object.isErrorIndicated()) {
                            throw new RuntimeException("Failed push: " + object.getErrorDetail());
                        }
                    }
                })
                .awaitCompletion();
    }

    @DynamicPropertySource
    static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.liquibase.enabled", () -> true);
    }
}
