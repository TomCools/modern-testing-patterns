package be.tomcools.moderntestingpatterns.app.equals;

import be.tomcools.moderntestingpatterns.app.velo.VeloStation;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// Using Equalsverifier
public class TestingEqualsAndHashcode {

    @Test
    @Disabled("Manual")
    public void testEqualsAndHashcode() {
        EqualsVerifier.forClass(VeloStation.class)
                .verify();
    }
}
