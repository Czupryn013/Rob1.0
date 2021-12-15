package pl.czupryn.rob.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.czupryn.rob.exceptions.UserNotFoundException;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.model.UserDto;
import pl.czupryn.rob.users.model.UserFullDto;
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

    @GetMapping(path = "/me")
    public ResponseEntity<UserFullDto> getUser() {
        Long myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User user = userService.findUserById(myId);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {

            return new ResponseEntity<>(UserFullDto.from(user), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me/password")
    public ResponseEntity<String> getPassword() {
        Long myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User user = userService.findUserById(myId);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me/username")
    public ResponseEntity<String> getUsername() {
        User user1 =((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String username = user1.getUsername();

        if (username == "anonymousUser") {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(username, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me/role")
    public ResponseEntity<String> getRole() {//@ControllerAdvice //mock
        User user1 =((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String username = user1.getUsername();

        if (username == "anonymousUser") {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user1.getRole().name(), HttpStatus.OK);
        }
    }


    //patch methods
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

    //friends methods

    @PreAuthorize("#username == authentication.name")
    @PostMapping(path = "/{username}/friends/add")
    public ResponseEntity<String> addFriend(@PathVariable String username, @RequestBody Long id) {
        Long myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userService.addFriend(myId,id);
        return null;
    }

    // post jeszcze nie zmieniam na users/me...
    @GetMapping(path = "/me/friends")
    public ResponseEntity<List<UserDto>> getFriends() {
        User user1 =((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String username = user1.getUsername();

        List<User> friends = userRepo.findByUsername(username).get().getFriends();
        List<UserDto> friendsDTO = new ArrayList<>();
        for (User friend : friends) {
            friendsDTO.add(UserDto.from(friend));
        }

        return new ResponseEntity<>(friendsDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/me/friends/{id}")
    public ResponseEntity<UserDto> getFriendById(@PathVariable Long id) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        List<User> friends = userRepo.findByUsername(username).get().getFriends();
        UserDto friendById = new UserDto();
        for (User friend : friends) {
            if (friend.getId() == id) {
                friendById = UserDto.from(friend);
            }
        }
        if (friendById.getUsername() != null && friendById.getId() != null) {
            return new ResponseEntity<>(friendById, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }


    }


}

