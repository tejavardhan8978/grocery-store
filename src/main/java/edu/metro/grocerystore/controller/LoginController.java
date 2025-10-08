package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private HomeController homeController;

    public LoginController(HomeController homeController) {
        this.homeController = homeController;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        User user = new User();
        user.setGuest(true);
        model.addAttribute(user);
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            Model model) {

        // Hardcoded credentials for now
        String validId = "user123";
        String validPassword = "password123";

        if (loginId.equals(validId) && password.equals(validPassword)) {
            // Create a User object to send to home page
            User user = new User();
            user.setFirstName("John Doe");

            model.addAttribute("user", user);
            return "home"; // home.html
        } else {
            model.addAttribute("error", "Invalid login ID or password");
            return "login"; // back to login page
        }
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        User user = new User();
        user.setGuest(true);
        model.addAttribute(user);
        return homeController.getHome(model);
    }
}
