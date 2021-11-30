package pl.czupryn.rob.users.service;

import org.springframework.http.ResponseEntity;
import pl.czupryn.rob.users.model.User;

import java.util.List;

public interface UserService {
    public ResponseEntity<String> saveUser(User user);

    ResponseEntity<List<User>> findAllUsers();

    List<User> findUsers();

    ResponseEntity<User> findUserById(Long id);
}
