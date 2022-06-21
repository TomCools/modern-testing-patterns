package be.tomcools.advancedtestingpatterns.testcontainers.additional.updatecontainer;

import be.tomcools.advancedtestingpatterns.app.velo.VeloRepository;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PushResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LiquibaseContainerUpdaterTest {

    /* NOTE
    A blog post about this solution is in the works... not going to comment everything here in the meantime ;)
     */

    private static final String DOCKER_IMAGE = "tomcools/postgres";
    private static final String IMAGE_TAG = "main";
    static final String FULL_IMAGE_NAME = "%s:%s".formatted(DOCKER_IMAGE, IMAGE_TAG);

    @Container
    private static final PostgreSQLContainer<?> SQL_CONTAINER = populateDatabase();

    @Autowired
    private VeloRepository repo;

    @Test
    @Disabled("Should be run only with certain profiles ;)")
    void pushNewImage() throws InterruptedException {
        // Startup and Liquibase happen first :-)

        // You can even use some code to add data!
        repo.save(VeloStation.builder().id("X").name("PRE_PROVISIONED").build());

        // Commit docker container changes into new image
        final DockerClient dockerClient = SQL_CONTAINER.getDockerClient();
        final String result = dockerClient.commitCmd(SQL_CONTAINER.getContainerId())
                .withRepository(DOCKER_IMAGE)
                .withTag("main")
                .exec();
        log.info(result);

        // Push new image
        dockerClient.pushImageCmd(FULL_IMAGE_NAME)
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(PushResponseItem object) {
                        // This is just to fail the build in case push of new image fails
                        log.info(object.toString());
                        if (object.isErrorIndicated()) {
                            throw new RuntimeException("Failed push: " + object.getErrorDetail());
                        }
                    }
                })
                .awaitCompletion();
    }

    @DynamicPropertySource
    private static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", SQL_CONTAINER::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
    }

    private static PostgreSQLContainer<?> populateDatabase() {
        try (PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:14.2")) {
            // map container data to "non volume path" as only those are saved on commit.
            // original volume path is "/var/lib/postgresql/data"
            return db.withEnv("PGDATA", "/var/lib/postgresql/container-data")
                    .withDatabaseName("testcontainer")
                    .withUsername("sa")
                    .withPassword("sa");
        }
    }
}
