package be.tomcools.advancedtestingpatterns;

import be.tomcools.advancedtestingpatterns.velo.VeloDataSync;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class AdvancedTestingPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedTestingPatternsApplication.class, args);
    }
}
