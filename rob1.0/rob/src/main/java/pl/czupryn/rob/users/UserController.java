package pl.czupryn.rob.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.users.User;
import pl.czupryn.rob.users.service.UserServiceImpl;
import java.util.List;

// przepisać na resty, czyli niech,
// zobacz jak działa spring security. Spróbuj autentykacji po bazie danych.
// UserdetailsService , UserDetails


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }
//    @GetMapping("/register")
//    public String get(Model model) {
//        return "register/register";
//    }

    @PostMapping("/register")
    @ResponseBody
    public String addUserToDB (@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    @ResponseBody
    public List<User> getAll() {
        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public User getById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

}

