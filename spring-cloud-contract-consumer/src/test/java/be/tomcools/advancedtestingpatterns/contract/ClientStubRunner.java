package be.tomcools.advancedtestingpatterns.contract;

import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = "be.tomcools:spring-cloud-contract:+:stubs:8095",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ClientStubRunner {

    @Test
    public void testRunStub() {
        final VeloStation resultingObject =
                new RestTemplate().getForObject("http://localhost:8095/velo/001", VeloStation.class);

        assertThat(resultingObject.getId()).isEqualTo("001");
    }

}
