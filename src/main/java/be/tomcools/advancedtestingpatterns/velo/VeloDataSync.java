package be.tomcools.advancedtestingpatterns.velo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

@ConditionalOnProperty(
        value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
@Configuration
@Slf4j
public class VeloDataSync {

    private final VeloClient client;
    private final VeloRepository repository;

    public VeloDataSync(VeloClient client, VeloRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    @Scheduled(fixedRate = 30000, initialDelay = 10000)
    public void runSync() {
        log.info("Velo Sync START");

         client.retrieveStations()
                .forEach(entry -> {
                    final Optional<VeloStation> inDb = repository.findById(entry.getId());
                    if(inDb.isPresent()) {
                        repository.save(merge(inDb.get(), entry));
                    } else {
                        repository.save(entry);
                    }
                });

        log.info("Velo Sync END");
    }

    private VeloStation merge(VeloStation fromDB, VeloStation fromApi) {
        fromDB.setBikes(fromApi.getBikes());
        fromDB.setSlots(fromApi.getSlots());
        fromDB.setStatus(fromApi.getStatus());
        fromDB.setStationType(fromApi.getStationType());
        return fromDB;
    }
}
