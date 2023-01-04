package be.tomcools.advancedtestingpatterns.wiremock;

import be.tomcools.advancedtestingpatterns.app.velo.VeloClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
public class VeloApiStubRecorder {

    @Autowired
    VeloClient veloClient;

    @Autowired
    WireMockServer wiremock;

    @Disabled("MANUAL UPDATE")
    @Test
    public void recordVeloApi() {
        wiremock.resetMappings(); // delete previous mappings
        wiremock.startRecording("https://www.velo-antwerpen.be/availability_map/getJsonObject");

        veloClient.retrieveStations();

        SnapshotRecordResult recordedMappings = wiremock.stopRecording();
        assertThat(recordedMappings.getStubMappings()).isNotEmpty();
    }
}
