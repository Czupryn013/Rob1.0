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

//    @GetMapping(path = "/me/username")
//    public ResponseEntity<String> getUsername() {
//        String username =((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        User user = userRepo.findByUsername(username).get();
//        if (user == null) {
//            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
//        }
//    }


    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/role")
    public ResponseEntity<String> getRole(@PathVariable String username) {//@ControllerAdvice //mock
        User user = userRepo.findByUsername(username).orElse(null);
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

    @PreAuthorize("#username == authentication.name")
    @PostMapping(path = "/{username}/friends/add")
    public ResponseEntity<String> addFriend(@PathVariable String username, @RequestBody Long id) {
        Long myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userService.addFriend(myId,id);
        return null;
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable String username) {
        List<User> friends = userRepo.findByUsername(username).get().getFriends();
        List<UserDto> friendsDTO = new ArrayList<>();
        for (User friend : friends) {
            friendsDTO.add(UserDto.from(friend));
        }

        return new ResponseEntity<>(friendsDTO, HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping(path = "/{username}/friends/{id}")
    public ResponseEntity<UserDto> getFriendById(@PathVariable String username, @PathVariable Long id) {
        List<User> friends = userRepo.findByUsername(username).get().getFriends();
        UserDto friendById = new UserDto();
        for (User friend : friends) {
            if (friend.getId() == id) {
                friendById = UserDto.from(friend);
            } else {
                friendById =  null;
            }
        }
        return new ResponseEntity<>(friendById, HttpStatus.OK);

    }


}

