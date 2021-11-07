package pl.czupryn.rob.users;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

@Controller
public class RegisterController {

    private User userToShow = new User();
    private String error = "";
    private String status = "";
    private boolean addNewUser = false;

    @GetMapping("/register")
    public String get(Model model) {
        model.addAttribute("user", userToShow);
        model.addAttribute("error", error);
        model.addAttribute("status", status);
        model.addAttribute("newUser", new User());
        return "register";
    }

    @PostMapping("/register-user")
    public String addUser(@ModelAttribute User user) {
        userToShow = user;
        if (!addNewUser) {
            status = "";
            addNewUser(user);
        }
        return "redirect:/register";
    }

    public void addNewUser(User userToAdd) {
        String username = userToAdd.getUsername();
        String password = userToAdd.getPassword();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rob1.0",
                        "root", "Alpaka123!@#$%");
                Statement statement = connection.createStatement();
                ResultSet rs;
                boolean incorrectPassword = false;

                username = username.replaceAll(" ", "_");//usuwanie wszystkich spacji z nicku

                rs = statement.executeQuery("SELECT username FROM users;");

                boolean okNick = true;
                while (rs.next()) {
                    if (rs.getString("username").equals(username)) {
//                        System.out.println("Ten nick jest zajęty : ( ");
                        error = "Ten nick jest zajęty";
                        okNick = false;
                        addNewUser = false;
                    } else if (okNick){
                        error = "";
                    }
                } //sprawdzanie czy nick jest zajety

                password = password.replaceAll(" ", "_"); //usuwanie spacji z hasła

                if (error == "") {
                    statement.executeUpdate("INSERT INTO users (username, password) VALUES('" + username + "', '"
                            + password + "');");
//                    System.out.println("Użytkownik " + username + " dodany!");
                    addNewUser = false;
                    userToShow = new User(username,password);
                    status = "Użytkownik " + username + " został dodany!";
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("lol coś nie działa, tyko co? : ) ");
            }


    }



}
