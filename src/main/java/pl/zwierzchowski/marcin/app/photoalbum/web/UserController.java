package pl.zwierzchowski.marcin.app.photoalbum.web;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.UserService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserModel;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserRegistrationModel;

@Controller
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
    ResponseEntity<UserEntity> registerUser(@RequestBody UserRegistrationModel registeredUser) {
        return ResponseEntity.ok(userService.register(registeredUser));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
