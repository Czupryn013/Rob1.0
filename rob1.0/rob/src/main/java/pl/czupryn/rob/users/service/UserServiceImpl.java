package pl.czupryn.rob.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.repository.UserRepo;
import pl.czupryn.rob.users.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

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
        user.setPassword(password);

        if (error == "") {
            userRepo.save(user);
            status = "Użytkownik " + user.getUsername() + " został dodany!";
            //System.out.println("Użytkownik " + username + " dodany!");
            return new ResponseEntity<>(url + " | " + status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
