package pl.czupryn.rob.users.service;

import org.springframework.transaction.annotation.Transactional;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;

import java.util.List;

public interface UserService {

    String saveUser(User user);

    List<User> findAllUsers();

    User findUserById(Long id);

    @Transactional
    String updatePassword(Long id, String password);

    @Transactional
    String updateUsername(Long id, String username);

    @Transactional
    String updateRole(Long userId, Role uRole);


}
