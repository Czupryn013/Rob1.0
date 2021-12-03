package pl.czupryn.rob.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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



//    USER AND ADMIN
    @Override
    public ResponseEntity<String> saveUser(User user) {
        String error = "";
        String status = "";
        String username = user.getUsername();
        String password = user.getPassword();
        String url = "users/"+ user.getId();
        List<User> allUsers = findUsers();

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
        System.out.println(error);

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        if (error == "") {
            userRepo.save(user);
            Optional<User> byUsername = userRepo.findByUsername(username);
            status = "Użytkownik " + user.getUsername() + " został dodany!";
            //System.out.println("Użytkownik " + username + " dodany!");
            return new ResponseEntity<>(url + " | " + status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    //ADMIN
    @Override
    public ResponseEntity<List<User>> findAllUsers() {
        try {
            return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public List<User> findUsers() {
        return userRepo.findAll();
    }

//    ADMIN
    @Override
    public ResponseEntity<User> findUserById(Long id) {
        try {
            Optional<User> userById = userRepo.findById(id);
            return new ResponseEntity<>(userById.get(), HttpStatus.OK);
//            throw new RuntimeException("User nie istnieje w bazie danych");
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}
