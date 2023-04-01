package be.tomcools.moderntestingpatterns.app.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static be.tomcools.moderntestingpatterns.app.user.UserResourceIntegrationTest.TestBuilder.anAdultUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserResourceIntegrationTest {

    UserResource sut = new UserResource();

    @Test
    @Disabled
    public void givenAnAdultUserConstructor_canRegister() {
        final LocalDate adultBirthDate = LocalDate.of(2000, 1, 1);
        final User adult = new User(UUID.randomUUID(), "An", "Adult", adultBirthDate);

        final ResponseEntity entity = sut.addOne(adult);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    @Disabled
    public void givenAnAdultUser_canRegister() {
        User testUser = mock(User.class);
        when(testUser.isAdult()).thenReturn(true);

        final ResponseEntity entity = sut.addOne(testUser);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    @Disabled
    public void givenAnAdultUserBuilder_canRegister() {
        final User adultUser = anAdultUser().build();

        final ResponseEntity entity = sut.addOne(adultUser);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }


    /* And example of a TestBuilder pattern. This is used to build domain objects in a structured way */
    class TestBuilder {
        public static User.UserBuilder aUser() {
            return User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .uuid(UUID.fromString("John Doe"))
                    .birthdate(LocalDate.of(2000, 10,31));
        }

        public static User.UserBuilder anAdultUser() {
            return User.builder()
                    .birthdate(LocalDate.of(2000, 01,01));
        }

        // Persona Alice
        public static User.UserBuilder alice() {
            return User.builder()
                    .firstName("Alice")
                    .lastName("Fries")
                    .uuid(UUID.fromString("Alice Fries"))
                    .birthdate(LocalDate.of(1991, 7,8));
        }

        // Persona Bob
        public static User.UserBuilder bob() {
            return User.builder()
                    .firstName("Bob")
                    .lastName("French")
                    .uuid(UUID.fromString("Bob French"))
                    .birthdate(LocalDate.of(1989, 10,31));
        }
    }
}
