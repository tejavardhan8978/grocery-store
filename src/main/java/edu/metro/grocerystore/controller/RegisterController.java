package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController extends BaseController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        // Check if user is already logged in, redirect to home
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null && !loggedInUser.isGuest()) {
            return "redirect:/";
        }
        
        // Create a new user object for the form (override the user from BaseController for this specific use case)
        User formUser = new User();
        model.addAttribute("user", formUser);
        
        // BaseController automatically adds current user (guest) to model
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("user") User user,
            Model model,
            HttpSession session) {

        try {
            // Check if email already exists
            if (userService.emailExists(user.getEmail())) {
                model.addAttribute("error", "Email already exists. Please use a different email.");
                return "register";
            }

            // Validate required fields
            if (user.getFirstName() == null || user.getFirstName().trim().isEmpty() ||
                user.getLastName() == null || user.getLastName().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                
                model.addAttribute("error", "All fields are required.");
                return "register";
            }

            // Register the user
            User registeredUser = userService.registerUser(user);

            // Automatically log in the user after successful registration
            session.setAttribute("loggedInUser", registeredUser);
            session.setAttribute("isLoggedIn", true);

            // Redirect to home page
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "register";
        }
    }
}
