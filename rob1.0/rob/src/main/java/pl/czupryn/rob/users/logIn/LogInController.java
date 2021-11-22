package pl.czupryn.rob.users.logIn;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.czupryn.rob.users.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

@Controller
public class LogInController {
    private String error = "";
    private String status = "";
    private User actualUser = new User();
    private boolean logInNewUser = false;

    @GetMapping("/login")
    public String get(Model model) {
        model.addAttribute("error", error);
        model.addAttribute("userToLogIn", new User());
        model.addAttribute("actualUser", actualUser);
        model.addAttribute("status", status);
        return "login";
    }

    @PostMapping("/login-user")
    public String logInUser(@ModelAttribute User user) {
        if (!logInNewUser) {
            actualUser = new User();
            status = "";
            logInNewUser(user);
        }

        return "redirect:/login";
    }

    public void logInNewUser(User userToLog) {
        boolean userLogIn = false;
        String username = userToLog.getUsername();
        String password = userToLog.getPassword();
        username = username.replaceAll(" ", "_");
        password = password.replaceAll(" ", "_");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rob1.0",
                    "root", "Alpaka123!@#$%");
            Statement statement = connection.createStatement();

            ResultSet rs;
            String sql;

            sql = "SELECT * FROM users WHERE username = '" +username + "' AND password = '" + password +"';";

            rs = statement.executeQuery(sql);

            while (rs.next()) {
                status = "Udało się zalogować! \n" +
                        "Nick: "+ rs.getString("username") + "\nId: " +rs.getString("id");
                actualUser = new User(username, password);
                userLogIn = true;
                error = "";

            }

            if (!rs.next() && !userLogIn) {
                error = "Nie poprawny nick lub hasło";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("lol coś nie działa, tyko co? : ) ");
        }

    }
}
