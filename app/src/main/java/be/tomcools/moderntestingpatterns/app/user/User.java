package be.tomcools.moderntestingpatterns.app.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
public class User {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;

    public boolean isAdult() {
        return birthdate.isBefore(LocalDate.now().minusYears(18));
    }
}
