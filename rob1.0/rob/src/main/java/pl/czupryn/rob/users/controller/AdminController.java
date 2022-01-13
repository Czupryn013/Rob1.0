package pl.czupryn.rob.users.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.model.UserDto;
import pl.czupryn.rob.users.repository.UserRepo;
import pl.czupryn.rob.users.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserRepo userRepo;

    public AdminController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> allUsersList = userService.findAllUsers();
        if (allUsersList == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            List<UserDto> allUsers = new ArrayList<>();
            for (User user: allUsersList ) {
                allUsers.add(UserDto.from(user));
            }
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            UserDto userDto = UserDto.from(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/users/{username}/role")
    public ResponseEntity<String> patchRole(@PathVariable String username, @RequestBody Role role) {
        try {
            String status = userService.updateRole(userRepo.findByUsername(username).get().getId(), role);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "users/delete")
    public ResponseEntity<String> deleteById(@RequestBody Long id){
        try {
            userRepo.deleteById(id);
            return new ResponseEntity<>("user with id: " + id + " deleted", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
