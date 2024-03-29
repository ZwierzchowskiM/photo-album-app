package pl.zwierzchowski.marcin.app.photoalbum.web;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.service.UserService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserModel;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    ResponseEntity<UserModel> getUser(@PathVariable Long id) {

        UserModel user = userService.findUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/")
    ResponseEntity<UserModel> registerUser(@Valid @RequestBody UserModel registeredUser) {
        return ResponseEntity.ok(userService.register(registeredUser));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
