package be.tomcools.advancedtestingpatterns.app.velo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VeloClientTest {

    @Autowired
    VeloClient veloClient;

    @Test
    void givenVeloClient_whenRetrievingAllStations_callsActualAPI() {
        final List<VeloStation> veloStations = veloClient.retrieveStations();

        Assertions.assertThat(veloStations).isNotEmpty();
    }
}
