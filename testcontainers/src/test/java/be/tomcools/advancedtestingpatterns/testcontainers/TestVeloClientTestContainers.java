package be.tomcools.advancedtestingpatterns.testcontainers;

import be.tomcools.advancedtestingpatterns.app.velo.VeloClient;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {TestVeloClientTestContainers.Initializer.class})
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
                    .withFileFromClasspath(".", "velotestcontainer/"));

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final String url = createVeloUrl();
            TestPropertyValues.of(
                    "app.velo.url=" + url
            ).applyTo(configurableApplicationContext.getEnvironment());
        }

        private String createVeloUrl() {
            return String.format("http://%s:%d/velodata", VELOCONTAINER.getContainerIpAddress(), VELOCONTAINER.getMappedPort(80));
        }
    }
}
