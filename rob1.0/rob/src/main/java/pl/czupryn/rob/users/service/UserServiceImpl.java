package pl.czupryn.rob.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.czupryn.rob.exceptions.UserNotFoundException;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;
import pl.czupryn.rob.users.repository.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String saveUser(User user) {
        String error = "";
        String status = "";
        String username = user.getUsername();
        String password = user.getPassword();
        String url;
        List<User> allUsers = findAllUsers();

        username = username.replaceAll(" ", "_");//usuwanie wszystkich spacji z nicku
        password = password.replaceAll(" ", "_"); //usuwanie spacji z hasła

        boolean okNick = true;
        for (User user1: allUsers) {
            if (user1.getUsername().equals(username)) {
                error = "Ten nick jest zajęty";
                System.out.println(error);
                okNick = false;
            } else if (okNick){
                error = "";
            }
        }   //sprawdzanie czy nick jest zajety
            //jeżeli error jest inny niż "" to znaczy że nick powtarza się w db przyniajmniej raz
        if (password.length() < 5 || password.length() > 24){error = "Hasło ma nieodpowiednią długość";}
        if (username.length() < 3 || username.length() > 20){error = "Nick ma nieodpowiednią długość";}

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        if (error == "") {
            userRepo.save(user);
            User user1 = userRepo.findByUsername(username).get();
            url = "url: users/"+ user.getId();
            status = "Użytkownik " + user.getUsername() + " został dodany!";
            return url + " | " + status;
        } else {
            return "error: " + error;
        }
    }

    @Override
    public List<User> findAllUsers() {
        try {
            return userRepo.findAll();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public User findUserById(Long id) {
        try {
            Optional<User> userById = userRepo.findById(id);
            return userById.get();
//            return new ResponseEntity<>(userById.get(), HttpStatus.OK);
        } catch(NoSuchElementException e) {
//            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return null;
        }
    }

    @Override
    public String updatePassword(Long id, String password) {
        try {
            password = password.replaceAll(" ", "_"); //usuwanie spacji z hasła
            if (password.length() < 5 || password.length() > 24){
                return "error: Hasło ma nieodpowiednią długość";
            } else {
                password = new BCryptPasswordEncoder().encode(password);
                userRepo.updatePassword(id, password);
                return "password updated";
            }
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public String updateUsername(Long id, String username) {
        try {
            List<User> allUsers = findAllUsers();

            String newUsername = username.replaceAll(" ", "_");//usuwanie wszystkich spacji z nicku
            boolean okNick = true;
            String error = "";
            for (User user1: allUsers) {
                if (user1.getUsername().equals(username)) {
                    error = "Ten nick jest zajęty";
                    System.out.println(error);
                    okNick = false;
                } else if (okNick){
                    error = "";
                }
            }
            if (username.length() < 3 || username.length() > 20){error = "Nick ma nieodpowiednią długość";}

            if (error == "") {
                userRepo.updateUsername(id, newUsername);
                String status = "Nick został zmieniony!";
                return status;
            } else {
                return "error: " + error;
            }
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public String updateRole(Long userId, Role uRole) {
        try {
            userRepo.updateRole(userId, uRole);
            return "role updated";
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public String addFriend(Long userId, Long friendId) {
        User friend = userRepo.findById(friendId).orElseThrow(() -> new UserNotFoundException("user not found"));

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        user.getFriends().add(friend);
        userRepo.save(user);
        return null;
    }
}


