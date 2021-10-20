package be.tomcools.advancedtestingpatterns.testcontainers;

import be.tomcools.advancedtestingpatterns.app.velo.VeloClient;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestVeloClientTestContainers {

    @Autowired
    VeloClient veloClient;

    @Test
    void givenVeloClient_whenRetrievingAllStations_callsActualAPIInTestcontainer() {
        final List<VeloStation> veloStations = veloClient.retrieveStations();

        Assertions.assertThat(veloStations).isNotEmpty();
    }

    @Container
    public static GenericContainer VELOCONTAINER = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromClasspath(".", "velotestcontainer/"))
                    .withExposedPorts(80);

    @DynamicPropertySource
    static void registerApiUrl(DynamicPropertyRegistry registry) {
        registry.add("app.velo.url", () -> String.format("http://%s:%d/velodata", VELOCONTAINER.getContainerIpAddress(), VELOCONTAINER.getMappedPort(80)));
    }

}
