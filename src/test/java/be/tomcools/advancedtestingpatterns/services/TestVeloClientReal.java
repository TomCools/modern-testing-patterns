package be.tomcools.advancedtestingpatterns.services;

import be.tomcools.advancedtestingpatterns.velo.VeloClient;
import be.tomcools.advancedtestingpatterns.velo.VeloStation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestVeloClientReal {

    @Autowired
    VeloClient veloClient;

    @Test
    void givenVeloClient_whenRetrievingAllStations_callsActualAPI() {
        final List<VeloStation> veloStations = veloClient.retrieveStations();

        assertThat(veloStations).isNotEmpty();
    }
}
