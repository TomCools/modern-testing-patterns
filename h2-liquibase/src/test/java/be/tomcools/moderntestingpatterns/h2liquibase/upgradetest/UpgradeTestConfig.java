package be.tomcools.moderntestingpatterns.h2liquibase.upgradetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class UpgradeTestConfig {

    @Autowired
    private DataSource dataSource;

    // FROM: https://stackoverflow.com/a/39015546
    //define this property in your embedded properties file or use spring's default
    @Value("${spring.liquibase.change-log:classpath:db/changelog/db.changelog-master.yaml}")
    private String defaultLiquibaseChangelog;

    @Bean
    public LiquibaseTestTemplate getUpgrader() {
        LiquibaseTestTemplate testTemplate = new LiquibaseTestTemplate();
        testTemplate.setDataSource(dataSource);
        testTemplate.setChangeLog(defaultLiquibaseChangelog);
        testTemplate.setShouldRun(false);
        return testTemplate;
    }
}
