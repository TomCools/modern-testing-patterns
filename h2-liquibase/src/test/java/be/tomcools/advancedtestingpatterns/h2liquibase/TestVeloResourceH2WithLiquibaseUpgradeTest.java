package be.tomcools.advancedtestingpatterns.h2liquibase;

import be.tomcools.advancedtestingpatterns.h2liquibase.upgradetest.LiquibaseTestTemplate;
import be.tomcools.advancedtestingpatterns.h2liquibase.upgradetest.UpgradeTestConfig;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.liquibase.enabled=true", "spring.datasource.url=jdbc:h2:mem:liquibaseupgrade"})
@Import(UpgradeTestConfig.class)
public class TestVeloResourceH2WithLiquibaseUpgradeTest {

    @Autowired
    LiquibaseTestTemplate liquibase;

    @Autowired
    DataSource dataSource;

    @Test
    public void givenRestClient_whenExecutingValidRequest_shouldReturnVelodata() {
        liquibase.runContext("!NEW");

        this.loadDatabaseScript("velo_station_dump.sql");

        liquibase.runContext("NEW");
    }

    private void loadDatabaseScript(String classPathResource) {
        Resource resource = new ClassPathResource(classPathResource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);
    }
}
