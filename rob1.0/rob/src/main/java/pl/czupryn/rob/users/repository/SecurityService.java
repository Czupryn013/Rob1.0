package pl.czupryn.rob.users.repository;


import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public boolean hasAccess(String username) {
        return false;
    }
}
