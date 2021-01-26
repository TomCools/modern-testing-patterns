package be.tomcools.advancedtestingpatterns.services.wiremock;

import be.tomcools.advancedtestingpatterns.velo.VeloClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class VeloApiStubRecorder {

    @Autowired
    VeloClient veloClient;

    @Autowired
    WireMockServer server;

    @Ignore("MANUAL UPDATE")
    @Test
    public void recordVeloApi() {
        server.resetMappings(); // delete previous mappings
        server.startRecording("https://www.velo-antwerpen.be/availability_map/getJsonObject");

        veloClient.retrieveStations();

        SnapshotRecordResult recordedMappings = server.stopRecording();
        assertThat(recordedMappings.getStubMappings()).isNotEmpty();
    }
}
