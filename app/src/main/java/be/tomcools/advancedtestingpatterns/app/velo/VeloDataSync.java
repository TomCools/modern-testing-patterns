package be.tomcools.advancedtestingpatterns.app.velo;

import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

@Configuration
@Slf4j
public class VeloDataSync {

    private final VeloClient client;
    private final VeloRepository repository;
    private final Boolean active;

    public VeloDataSync(VeloClient client, VeloRepository repository, @Value("${app.scheduling.enable:false}") Boolean active) {
        this.client = client;
        this.repository = repository;
        this.active = active;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 10000)
    public void runSync() {
        log.info("Velo Sync START");

        if (active) {
            runSyncDirect();
        }

        log.info("Velo Sync END");
    }

    public void runSyncDirect() {
        client.retrieveStations()
                .forEach(entry -> {
                    final Optional<VeloStation> inDb = repository.findById(entry.getId());
                    if (inDb.isPresent()) {
                        repository.save(merge(inDb.get(), entry));
                    } else {
                        repository.save(entry);
                    }
                });
    }

    private VeloStation merge(VeloStation fromDB, VeloStation fromApi) {
        return fromDB.toBuilder()
                .bikes(fromApi.getBikes())
                .slots(fromApi.getSlots())
                .status(fromApi.getStatus())
                .stationType(fromApi.getStationType())
                .build();
    }

    @Bean
    public Lombok injectableLombok() {
        return new Lombok();
    }
    // For Architecture example only. Don't do this!

}
