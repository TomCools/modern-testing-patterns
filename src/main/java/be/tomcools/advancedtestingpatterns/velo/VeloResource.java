package be.tomcools.advancedtestingpatterns.velo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/velo")
public class VeloResource {

    public VeloRepository repository;

    public VeloResource(VeloRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<VeloStation> getVeloStations() {
        return repository.findAll();
    }

    @GetMapping(path = "/{id}")
    public VeloStation getVeloStationById(@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping(path = "/addMany")
    public void addMany(Iterable<VeloStation> newStations) {
        repository.saveAll(newStations);
    }
}
