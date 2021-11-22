package pl.czupryn.rob.users.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.czupryn.rob.users.register.repository.UserRepo;
import pl.czupryn.rob.users.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public String saveUser(User user) {
        String error = "";
        String status = "";
        String username = user.getUsername();
        String password = user.getPassword();


        username = username.replaceAll(" ", "_");//usuwanie wszystkich spacji z nicku

        List<User> allUsers = findAllUsers();

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

        password = password.replaceAll(" ", "_"); //usuwanie spacji z hasła
        user.setUsername(username);
        user.setPassword(password);

        if (error == "") {
            userRepo.save(user);
            status = "Użytkownik " + user.getUsername() + " został dodany!";
            //System.out.println("Użytkownik " + username + " dodany!");
            return status;
        } else {
            return error;
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userById = userRepo.findById(id);
        if (userById.isPresent()){
            return userById.get();
        }
        throw new RuntimeException("User nie istnieje w bazie danych");
    }
}
