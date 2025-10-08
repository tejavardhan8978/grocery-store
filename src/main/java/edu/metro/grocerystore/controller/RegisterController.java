package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        User user = new User();
        user.setGuest(true);
        model.addAttribute(user);
        return "register";
    }
}
