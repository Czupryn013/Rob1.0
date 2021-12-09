package pl.czupryn.rob.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.czupryn.rob.users.model.Role;
import pl.czupryn.rob.users.model.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("update users set password = :uPassword where id = :userId")
    void updatePassword(Long userId, String uPassword);

    @Modifying
    @Query("update users set username = :uUsername where id = :userId")
    void updateUsername(Long userId, String uUsername);

    @Modifying
    @Query("update users set role = :uRole where id = :userId")
    void updateRole(Long userId, Role uRole);

}
