package be.tomcools.advancedtestingpatterns.app;

import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestingForCoverage {
    @Test
    public void testGetters() {
        final VeloStation station = VeloStation.builder()
                .id("1")
                .bikes("2")
                .lat("52.02")
                .lon("13.3")
                .name("KDG Campus")
                .zip("2000")
                .build();

        assertThat(station.getId()).isEqualTo("1");
        assertThat(station.getBikes()).isEqualTo("2");
        assertThat(station.getLat()).isEqualTo("52.02");
        assertThat(station.getLon()).isEqualTo("13.3");
        assertThat(station.getName()).isEqualTo("KDG Campus");
        assertThat(station.getZip()).isEqualTo("2000");
    }
}
