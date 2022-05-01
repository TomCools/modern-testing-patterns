package be.tomcools.advancedtestingpatterns.h2liquibase.other;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.liquibase.enabled=true"
        ,"spring.datasource.url=jdbc:h2:mem:liquibase-pos;MODE=PostgreSQL"
})
public class TestVeloResourceH2WithLiquibasePostgreSQL {

    @Autowired
    JdbcTemplate template;

    @Test
    public void whenInPostgreSQLCompatibilityMode_canInvokePostgreSQLSpecificCommand() {
        // This query only works on PostgreSQL, but in compatibility mode, H2 can deal with it.
        final String query = """
                         INSERT INTO velo_station(id, name)
                         VALUES(1,'test') 
                         ON CONFLICT DO NOTHING;
                         """;
        template.update(query);
        template.update(query);
    }
}
