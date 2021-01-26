package be.tomcools.advancedtestingpatterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdvancedTestingPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedTestingPatternsApplication.class, args);
    }
}
