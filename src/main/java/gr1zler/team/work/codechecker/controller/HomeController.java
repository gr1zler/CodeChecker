package gr1zler.team.work.codechecker.controller;

import gr1zler.team.work.codechecker.model.User;
import gr1zler.team.work.codechecker.service.AuthenticationService;
import gr1zler.team.work.codechecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService){
        this.userService=userService;
    }

    @GetMapping()
    public ResponseEntity<String> home() throws IOException {
        return ResponseEntity.ok("Welcome to codechecker");
    }

    @GetMapping("/profile")
    public ResponseEntity<User> profile(Authentication authentication) throws IOException {
        String email=authentication.getName();
        User user=userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }


}
