package be.tomcools.advancedtestingpatterns.database;

import be.tomcools.advancedtestingpatterns.velo.VeloResource;
import be.tomcools.advancedtestingpatterns.velo.VeloStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestVeloResourcePostgresWithLiquibase.Initializer.class})
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

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
