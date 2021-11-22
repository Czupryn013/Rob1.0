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
    public String get(Model model) {
//        model.addAttribute("user", userToShow);
//        model.addAttribute("error", error);
//        model.addAttribute("status", status);
//        model.addAttribute("newUser", new User());
        return "register";
    }

//    @PostMapping("/register-user")
//    public String addUser(@ModelAttribute User user) {
//        userToShow = user;
//        if (!addNewUser) {
//            status = "";
//            addNewUser(user);
//        }
//        return "redirect:/register";
//    }

    public void addNewUser(User userToAdd) {
        String username = userToAdd.getUsername();
        String password = userToAdd.getPassword();
        boolean incorrectPassword = false;

        username = username.replaceAll(" ", "_");//usuwanie wszystkich spacji z nicku

        List<User> allUsers = userService.findAllUsers();

        boolean okNick = true;
        for (User user: allUsers) {
            if (user.getUsername().equals(username)) {
                error = "Ten nick jest zajęty";
                okNick = false;
                addNewUser = false;
            } else if (okNick){
                error = "";
            }
        }
         //sprawdzanie czy nick jest zajety

        password = password.replaceAll(" ", "_"); //usuwanie spacji z hasła
        userToAdd.setUsername(username);
        userToAdd.setPassword(password);

        if (error == "") {
            userService.saveUser(userToAdd);
//                    System.out.println("Użytkownik " + username + " dodany!");
            addNewUser = false;
            userToShow = userToAdd;
            status = "Użytkownik " + userToAdd.getUsername() + " został dodany!";
        }


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

