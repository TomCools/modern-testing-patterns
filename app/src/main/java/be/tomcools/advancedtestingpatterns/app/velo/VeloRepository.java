package be.tomcools.advancedtestingpatterns.app.velo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeloRepository extends CrudRepository<VeloStation, String> {
}
