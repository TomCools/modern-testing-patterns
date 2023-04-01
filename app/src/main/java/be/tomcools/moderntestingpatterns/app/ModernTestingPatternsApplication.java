package be.tomcools.moderntestingpatterns.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "be.tomcools.moderntestingpatterns")
@EnableJpaRepositories(basePackages="be.tomcools.moderntestingpatterns.app")
@EntityScan(basePackages="be.tomcools.moderntestingpatterns.app")
public class ModernTestingPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModernTestingPatternsApplication.class, args);
    }
}
