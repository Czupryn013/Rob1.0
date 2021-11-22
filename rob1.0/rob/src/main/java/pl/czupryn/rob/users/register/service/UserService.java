package pl.czupryn.rob.users.register.service;

import pl.czupryn.rob.users.User;

import java.util.List;

public interface UserService {
    public String saveUser(User user);

    List<User> findAllUsers();

    User findUserById(Long id);


}
