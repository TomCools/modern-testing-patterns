package be.tomcools.advancedtestingpatterns.wiremock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "be.tomcools.advancedtestingpatterns")
@EnableJpaRepositories(basePackages="be.tomcools.advancedtestingpatterns.app.velo")
@EntityScan(basePackages="be.tomcools.advancedtestingpatterns.app.velo")
public class AdvancedTestingPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedTestingPatternsApplication.class, args);
    }
}
