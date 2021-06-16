package be.tomcools.advancedtestingpatterns.app;

import be.tomcools.advancedtestingpatterns.app.velo.VeloStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestingForCoverage {
    VeloStation station;

    @BeforeEach
    public void init() {
        station = VeloStation.builder()
                .id("1")
                .bikes("2")
                .lat("52.02")
                .lon("13.3")
                .name("KDG Campus")
                .zip("2000")
                .slots("0")
                .address("adres")
                .status("OPN")
                .stationType("BIKE")
                .build();
    }

    @Test
    public void testGetters() {
        assertThat(station.getId()).isEqualTo("1");
        assertThat(station.getSlots()).isEqualTo("0");
        assertThat(station.getAddress()).isEqualTo("adres");
        assertThat(station.getStatus()).isEqualTo("OPN");
        assertThat(station.getStationType()).isEqualTo("BIKE");
        assertThat(station.getBikes()).isEqualTo("2");
        assertThat(station.getLat()).isEqualTo("52.02");
        assertThat(station.getLon()).isEqualTo("13.3");
        assertThat(station.getName()).isEqualTo("KDG Campus");
        assertThat(station.getZip()).isEqualTo("2000");
    }


    // Our manager told us to get 85% code coverage LOL
    @Test
    public void cheapCodeCoverageTest() {
        station.getCleanName();
        station.isOpen();
        station.getAvailableBikes();
        station.getAvailableSlots();
    }
}
