package pl.czupryn.rob.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.service.UserServiceImpl;
import java.util.List;

// przepisać na resty, czyli niech,
// zobacz jak działa spring security. Spróbuj autentykacji po bazie danych.
// UserdetailsService , UserDetails
//@Controlleradvice

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String get() {
        return "register/register";
    }

    //nie ważne     ^
    //              |

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> addUserToDB (@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

}

