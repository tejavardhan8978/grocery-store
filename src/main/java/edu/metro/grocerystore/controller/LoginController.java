package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController extends BaseController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(Model model, HttpSession session) {
        // Check if user is already logged in, redirect to home
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null && !loggedInUser.isGuest()) {
            return "redirect:/";
        }
        
        // Create a guest user for header display
        User guestUser = userService.createGuestUser();
        model.addAttribute("user", guestUser);
        model.addAttribute("headerUser", guestUser);
        
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("loginId") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        try {
            // Authenticate user using the service
            Optional<User> authenticatedUser = userService.authenticateUser(email, password);
            
            if (authenticatedUser.isPresent()) {
                User user = authenticatedUser.get();
                
                // Store user in session
                session.setAttribute("loggedInUser", user);
                session.setAttribute("isLoggedIn", true);
                
                // Add user to model for immediate use
                model.addAttribute("user", user);
                
                // Redirect to home page
                return "redirect:/";
            } else {
                // Authentication failed
                model.addAttribute("error", "Invalid email or password");
                User guestUser = userService.createGuestUser();
                model.addAttribute("user", guestUser);
                return "login";
            }
        } catch (Exception e) {
            // Handle any database or service errors
            model.addAttribute("error", "An error occurred during login. Please try again.");
            User guestUser = userService.createGuestUser();
            model.addAttribute("user", guestUser);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        // Clear session data
        session.removeAttribute("loggedInUser");
        session.removeAttribute("isLoggedIn");
        session.invalidate();
        
        // Redirect to home page (which will show guest user)
        return "redirect:/";
    }
}
