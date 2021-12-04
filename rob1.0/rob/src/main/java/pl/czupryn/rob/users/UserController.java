package pl.czupryn.rob.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String status = userService.saveUser(user);
        if (status.charAt(0) == 'e') {
            return new ResponseEntity<>(status, HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsersList = userService.findAllUsers();
        if (allUsersList == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allUsersList, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user1 = userService.findUserById(id);
        if (user1 == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user1, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<User> getUsername(@PathVariable String username) {
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}

