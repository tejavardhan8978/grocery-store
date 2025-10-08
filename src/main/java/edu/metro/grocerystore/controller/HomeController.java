package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private User user;
    private boolean isLoggedIn;


    @GetMapping("/")
    public String getHome(Model model) {
        if (isLoggedIn == false) {
            user = new User();
            user.setGuest(true);
            model.addAttribute(user);
            return "home";
        } else {
            model.addAttribute(user);
            return "home";
        }
    }

}
