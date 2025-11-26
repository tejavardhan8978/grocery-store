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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController extends BaseController {

    private static final String LOGGED_IN_USER_ATTR = "loggedInUser";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Display user profile page
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        // Refresh user data from database to get latest info
        User currentUser = userService.getUserById(loggedInUser.getId())
            .orElse(loggedInUser);

        model.addAttribute("user", currentUser);
        return "profile";
    }

    /**
     * Update user profile
     */
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String zipCode,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        try {
            // Get user from database
            User user = userService.getUserById(loggedInUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Update user details
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setCity(city);
            user.setState(state);
            user.setZipCode(zipCode);

            // Save to database
            userService.updateUser(user);

            // Update session
            session.setAttribute(LOGGED_IN_USER_ATTR, user);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}
