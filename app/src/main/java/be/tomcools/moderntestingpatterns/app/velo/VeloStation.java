package be.tomcools.moderntestingpatterns.app.velo;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@Entity
public class VeloStation {
    @Id
    private String id;
    private String lon;
    private String lat;
    private String bikes;
    private String slots;
    private String zip;
    private String address;
    private String status;
    private String name;
    private String stationType;

    public VeloStation() {
    }

    public String getCleanName() {
        return this.name.replaceFirst(this.id, "").replaceFirst("- ", "");
    }

    public boolean isOpen() {
        return "OPN".equalsIgnoreCase(status);
    }

    public int getAvailableBikes() {
        return Integer.parseInt(bikes);
    }

    public int getAvailableSlots() {
        return Integer.parseInt(slots);
    }
}
