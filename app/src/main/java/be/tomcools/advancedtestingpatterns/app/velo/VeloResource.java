package be.tomcools.advancedtestingpatterns.app.velo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
