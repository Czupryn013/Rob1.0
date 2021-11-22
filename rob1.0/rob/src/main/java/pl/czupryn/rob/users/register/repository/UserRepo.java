package pl.czupryn.rob.users.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.czupryn.rob.users.User;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

}
