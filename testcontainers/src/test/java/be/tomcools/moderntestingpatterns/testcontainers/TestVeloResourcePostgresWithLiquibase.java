package be.tomcools.moderntestingpatterns.testcontainers;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import be.tomcools.moderntestingpatterns.app.velo.VeloResource;
import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = ModernTestingPatternsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestVeloResourcePostgresWithLiquibase {

    private static final Iterable<VeloStation> TEST_VELO_STATIONS = List.of(
            VeloStation.builder().id("1").name("TESTNAME_1").build(),
            VeloStation.builder().id("2").name("TESTNAME_2").build());

    @Autowired
    VeloResource sut;

    @BeforeEach
    public void setup() {
        sut.addMany(TEST_VELO_STATIONS);
    }

    @Test
    public void givenRestClient_whenExecutingValidRequest_shouldReturnVelodata() {
        final Iterable<VeloStation> result = sut.getVeloStations();

        assertThat(result).isEqualTo(TEST_VELO_STATIONS);
    }

    //https://github.com/spring-projects/spring-boot/issues/34658
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerTestContainerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
        registry.add("spring.liquibase.enabled", () -> true);
    }
}
