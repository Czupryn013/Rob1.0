package pl.czupryn.rob.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.czupryn.rob.users.model.User;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

}
