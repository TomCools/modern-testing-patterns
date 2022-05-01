package be.tomcools.advancedtestingpatterns.app.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    @PostMapping()
    public ResponseEntity addOne(User newUser) {
        if(newUser.isAdult()) {
            // actually do some processing;
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().body("Underaged users are not allowed");
        }
    }
}
