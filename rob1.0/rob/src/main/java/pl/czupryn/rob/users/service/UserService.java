package pl.czupryn.rob.users.service;

import org.springframework.http.ResponseEntity;
import pl.czupryn.rob.users.model.User;

import java.util.List;

public interface UserService {

    String saveUser(User user);

    List<User> findAllUsers();

    User findUserById(Long id);
}
