package be.tomcools.advancedtestingpatterns.h2liquibase;

import be.tomcools.advancedtestingpatterns.app.velo.VeloResource;
import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.liquibase.enabled=true","spring.datasource.url=jdbc:h2:mem:liquibase"})
public class TestVeloResourceH2WithLiquibase {

    private static final Iterable<VeloStation> TEST_VELO_STATIONS = List.of(
            VeloStation.builder().id("1").name("TESTNAME_1").build(),
            VeloStation.builder().id("2").name("TESTNAME_2").build());

    @Autowired
    VeloResource sut;

    @BeforeEach
    public void setup() {
        sut.addMany(TEST_VELO_STATIONS);
    }

    @Test
    public void givenRestClient_whenExecutingValidRequest_shouldReturnVelodata() {
        final Iterable<VeloStation> result = sut.getVeloStations();

        assertThat(result).isEqualTo(TEST_VELO_STATIONS);
    }
}
