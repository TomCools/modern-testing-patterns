package be.tomcools.moderntestingpatterns.contract;

import be.tomcools.moderntestingpatterns.app.ModernTestingPatternsApplication;
import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest(classes = ModernTestingPatternsApplication.class)
@AutoConfigureStubRunner(ids = "be.tomcools:spring-cloud-contract:+:stubs:8095",
        stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
public class ClientStubRunnerTest {

    @Test
    public void givenVeloId_whenIGetTheDetailsOfThatVelo_idMatchesExpectedValue() {
        when().
                get("http://localhost:8095/velo/001").
        then()
                .statusCode(200)
                .body("id", equalTo("001"));
    }

}
