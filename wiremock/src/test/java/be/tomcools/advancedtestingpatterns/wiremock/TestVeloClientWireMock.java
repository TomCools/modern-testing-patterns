package be.tomcools.advancedtestingpatterns.wiremock;

import be.tomcools.advancedtestingpatterns.app.velo.VeloClient;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
class TestVeloClientWireMock {

    @Autowired
    VeloClient veloClient;

    @Autowired
    WireMockServer wireMockServer;

    @BeforeEach
    public void resetWireMock() {
        wireMockServer.resetToDefaultMappings();
    }

    @Test
    void givenNoStations_whenRetrievingAllStations_resultingListIsEmpty() {
        wireMockServer.stubFor(WireMock.get("/")
                .willReturn(okJson("[]")));

        final List<VeloStation> veloStations = veloClient.retrieveStations();

        assertThat(veloStations).isEmpty();
    }

    @Test
    void givenStations_whenRetrievingAllStations_resultingListContainsItems() {
        wireMockServer.stubFor(WireMock.get("/")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("velodata.json")));

        final List<VeloStation> veloStations = veloClient.retrieveStations();

        assertThat(veloStations).hasSize(2); //in velodata.json
    }

    @Test
    void givenStations_whenRetrievingAllStationsHasBigDelay_resultingListContainsItems() {
        wireMockServer.stubFor(WireMock.get("/")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("velodata.json")
                        .withFixedDelay(200)));

        final List<VeloStation> veloStations = veloClient.retrieveStations();

        assertThat(veloStations).hasSize(2); //in velodata.json
    }

    @Test
    void givenStationsStub_whenRetrievingAllStations_resultingListContainsItems() {
        // MAGIC (not really... check /test/resources/mapping)

        final List<VeloStation> veloStations = veloClient.retrieveStations();

        assertThat(veloStations).isNotEmpty();
    }
}
