package be.tomcools.moderntestingpatterns.testcontainers.additional;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import be.tomcools.moderntestingpatterns.app.velo.VeloClient;
import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

@Slf4j
@Testcontainers
@SpringBootTest(classes = ModernTestingPatternsApplication.class)
class TestBreakingVeloClientToxiProxy {

    @Autowired
    VeloClient veloClient;

    @RepeatedTest(10)
    void givenVeloClient_whenRetrievingAllStations_callsActualAPIInTestcontainer() {
        final List<VeloStation> veloStations = veloClient.retrieveStations();

        Assertions.assertThat(veloStations).isNotEmpty();
    }

    static Network network = Network.newNetwork();

    @Container
    public static GenericContainer VELOCONTAINER = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromClasspath(".", "velotestcontainer/"))
            .withNetwork(network);

    @Container
    public static ToxiproxyContainer TOXIPROXY = new ToxiproxyContainer("shopify/toxiproxy:2.1.0")
            .withNetwork(network);

    @DynamicPropertySource
    static void registerApiUrl(DynamicPropertyRegistry registry) throws IOException {
        var proxy = TOXIPROXY.getProxy(VELOCONTAINER, 80);
        registry.add("app.velo.url", () -> String.format("http://%s:%d/velodata", proxy.getContainerIpAddress(), proxy.getProxyPort()));
        proxy.toxics()
                .latency("latency",ToxicDirection.DOWNSTREAM, 20)
                .setJitter(500);
    }


}
