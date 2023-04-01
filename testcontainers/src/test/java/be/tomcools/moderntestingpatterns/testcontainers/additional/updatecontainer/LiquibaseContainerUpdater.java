package be.tomcools.moderntestingpatterns.testcontainers.additional.updatecontainer;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import be.tomcools.moderntestingpatterns.app.velo.VeloRepository;
import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PushResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest(classes = ModernTestingPatternsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Profile("update-container") //You only want this to run on a specific branch ;) probably main/dev!
public class LiquibaseContainerUpdater {

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
    public void pushNewImage() throws InterruptedException {
        // Startup and Liquibase happen first :-)

        // You can even use some code to add data!
        repo.save(VeloStation.builder().id("X").name("PRE_PROVISIONED").build());

        // Commit docker container changes into new image
        final DockerClient dockerClient = postgreSQLContainer.getDockerClient();
        dockerClient.commitCmd(postgreSQLContainer.getContainerId())
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
    // Soon to be replaced by a single @JdbcServiceConnection annotation ;) https://github.com/spring-projects/spring-boot/commit/95f45eab1fc1a2af5697812e655b338dc8806ccc#diff-1cc8ac5dd456765d5ced551a2da29e0cd1b7a0e75441742b9cac600fb63de726
}
