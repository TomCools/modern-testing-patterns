package be.tomcools.moderntestingpatterns.app;

import be.tomcools.moderntestingpatterns.app.velo.VeloRepository;
import be.tomcools.moderntestingpatterns.app.velo.VeloResource;
import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;

public class MocksTest {

    private static final Iterable<VeloStation> TEST_VELO_STATIONS = List.of(VeloStation.builder().id("1").build(),
            VeloStation.builder().id("2").build());

    VeloResource sut;

    @BeforeEach
    public void setup() {
        final VeloRepository repository = Mockito.mock(VeloRepository.class);

        when(repository.findAll())
                .thenReturn(TEST_VELO_STATIONS);

        sut = new VeloResource(repository);
    }

    @Test
    public void givenRestClient_whenExecutingValidRequest_shouldReturnVelodata() {
        final Iterable<VeloStation> result = sut.getVeloStations();

        Assertions.assertThat(result).isEqualTo(TEST_VELO_STATIONS);
    }
}
