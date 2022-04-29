package be.tomcools.advancedtestingpatterns.testcontainers.additional;

import com.github.dockerjava.api.DockerClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LiquibaseContainerUpdater {

    private static DockerImageName IMAGE =  DockerImageName.parse("tomcools/postgres:dev")
            .asCompatibleSubstituteFor("postgres");
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(IMAGE)
            .withImagePullPolicy(PullPolicy.alwaysPull())
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    @Test
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
                .start().awaitCompletion();
    }

    @DynamicPropertySource
    static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.liquibase.enabled", () -> true);
    }
}
