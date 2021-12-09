package pl.czupryn.rob.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.exceptions.UserNotFoundException;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.model.UserDto;
import pl.czupryn.rob.users.repository.UserRepo;
import pl.czupryn.rob.users.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// przepisać na resty, czyli niech,
// zobacz jak działa spring security. Spróbuj autentykacji po bazie danych.
// UserdetailsService , UserDetails
//@Controlleradvice

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserRepo userRepo;

    @Autowired
    public UserController(UserServiceImpl userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping("/register")
    public String get() {
        return "register/register";
    }
    //nie ważne     ^
    //              |


    //ogólne
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


    //tylko użytkownik ma dostęp do metod poniżej

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userRepo.findByUsername(username).get();
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/password")
    public ResponseEntity<String> getPassword(@PathVariable String username) {
        User user = userRepo.findByUsername(username).get();
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);
        }
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/username")
    public ResponseEntity<String> getUsername(@PathVariable String username) {
        User user = userRepo.findByUsername(username).get();
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
        }
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/role")
    public ResponseEntity<String> getRole(@PathVariable String username) {
        User user = userRepo.findByUsername(username).get();
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user.getRole().name(), HttpStatus.OK);
        }
    }

    @PreAuthorize("#username == authentication.name")
    @PatchMapping(path = "/{username}/password")
    public ResponseEntity<String> patchPassword(@PathVariable String username, @RequestBody String password) {
        try {
            String status = userService.updatePassword(userRepo.findByUsername(username).get().getId(), password);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("#username == authentication.name")
    @PatchMapping(path = "/{username}/username")
    public ResponseEntity<String> patchUsername(@PathVariable String username, @RequestBody String userName) {
        try {
            String status = userService.updateUsername(userRepo.findByUsername(username).get().getId(), userName);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}

