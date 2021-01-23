package be.tomcools.advancedtestingpatterns.contracttests;

import be.tomcools.advancedtestingpatterns.services.wiremock.WireMockInitializer;
import be.tomcools.advancedtestingpatterns.velo.VeloDataSync;
import be.tomcools.advancedtestingpatterns.velo.VeloResource;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {WireMockInitializer.class, ContractVerifierBase.Initializer.class})
public abstract class ContractVerifierBase {

    /*@Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("testcontainer")
            .withUsername("sa")
            .withPassword("sa");*/

    @Autowired
    VeloResource veloResource;

    @Autowired
    VeloDataSync sync;

    @BeforeEach
    public void before() {
        // Force sync before test start
        sync.runSync();

        RestAssuredMockMvc.standaloneSetup(this.veloResource);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                   /* T  "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.liquibase.enabled=true",*/
                    "app.scheduling.enable=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
