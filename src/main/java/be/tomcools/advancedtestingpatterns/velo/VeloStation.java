package be.tomcools.advancedtestingpatterns.velo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@Data
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
