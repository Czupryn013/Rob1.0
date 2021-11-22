package pl.czupryn.rob.users.register;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.users.User;
import pl.czupryn.rob.users.register.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/users")
public class RegisterController {

    private final UserServiceImpl userService;
    private User userToShow = new User();
    private String error = "";
    private String status = "";
    private boolean addNewUser = false;

    @Autowired
    public RegisterController(UserServiceImpl userService) {
        this.userService = userService;

    }

    @GetMapping("/register")
    public String get() {
        return "register";
    }

    //nowe
    @PostMapping
    @ResponseBody
    public String addUserToDB (@RequestBody User user) {
//        userService.saveUser(user);
//        return true;
        return userService.saveUser(user);
    }

    @GetMapping("/all")
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

