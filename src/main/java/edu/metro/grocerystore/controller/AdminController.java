package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    
    // Constants for repeated strings
    private static final String LOGGED_IN_USER_ATTR = "loggedInUser";
    private static final String ADMIN_MODE_ATTR = "adminMode";
    private static final String HEADER_USER_ATTR = "headerUser";
    private static final String ERROR_ATTR = "error";
    private static final String SUCCESS_ATTR = "success";
    private static final String REDIRECT_HOME = "redirect:/";
    private static final String REDIRECT_ADMIN_CUSTOMERS = "redirect:/admin/customers";
    private static final String REDIRECT_ADMIN_CUSTOMER_DETAILS = "redirect:/admin/customers/";
    private static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found.";
    private static final String EMPLOYEE_ROLE = "employee";
    private static final String ADMIN_ROLE = "admin";
    
    private final UserService userService;
    
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Toggle between admin mode and user mode
     */
    @PostMapping("/toggle-mode")
    public String toggleAdminMode(HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        // Check if user is logged in and is an admin
        if (loggedInUser == null || !loggedInUser.isAdmin()) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Access denied. Admin privileges required.");
            return REDIRECT_HOME;
        }
        
        // Toggle admin mode
        Boolean currentAdminMode = (Boolean) session.getAttribute(ADMIN_MODE_ATTR);
        boolean newAdminMode = currentAdminMode == null || !currentAdminMode;
        session.setAttribute(ADMIN_MODE_ATTR, newAdminMode);
        
        String message = newAdminMode ? "Switched to Admin Mode" : "Switched to User Mode";
        redirectAttributes.addFlashAttribute(SUCCESS_ATTR, message);
        
        // Redirect to appropriate page based on mode
        if (newAdminMode) {
            return "redirect:/admin/dashboard";
        } else {
            return REDIRECT_HOME;
        }
    }
    
    /**
     * Admin dashboard home page
     */
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdminMode(session, redirectAttributes)) {
            return REDIRECT_HOME;
        }
        
        // BaseController automatically adds user to model
        
        return "admin/dashboard";
    }
    
    /**
     * Customers list page with search functionality
     */
    @GetMapping("/customers")
    public String customersList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        // BaseController automatically adds user to model
        
        // Create pageable with sorting
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> usersPage;
        if (search != null && !search.trim().isEmpty()) {
            usersPage = userService.searchUsers(search.trim(), pageable);
        } else {
            usersPage = userService.getAllUsers(pageable);
        }
        
        // Add attributes to model
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalElements", usersPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("search", search);
        
        return "admin/customers";
    }
    
    /**
     * Customer details page
     */
    @GetMapping("/customers/{id}")
    public String customerDetails(
            @PathVariable Integer id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        // BaseController automatically adds user to model
        
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Customer not found.");
            return "redirect:/admin/customers";
        }
        
        User customer = userOpt.get();
        model.addAttribute("customer", customer);
        
        return "admin/customer-details";
    }
    
    /**
     * Delete a customer
     */
    @PostMapping("/customers/{id}/delete")
    public String deleteCustomer(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Prevent admin from deleting themselves
        if (loggedInUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "You cannot delete your own account.");
            return "redirect:/admin/customers/" + id;
        }
        
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Customer not found.");
            return "redirect:/admin/customers";
        }
        
        User customer = userOpt.get();
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", 
                "Customer " + customer.getFirstName() + " " + customer.getLastName() + " has been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting customer: " + e.getMessage());
            return "redirect:/admin/customers/" + id;
        }
        
        return "redirect:/admin/customers";
    }
    
    /**
     * Make a customer an employee
     */
    @PostMapping("/customers/{id}/make-employee")
    public String makeEmployee(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        return updateUserRole(id, "employee", true, redirectAttributes);
    }
    
    /**
     * Remove employee status from a user
     */
    @PostMapping("/customers/{id}/remove-employee")
    public String removeEmployee(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        return updateUserRole(id, "employee", false, redirectAttributes);
    }
    
    /**
     * Make a customer an admin
     */
    @PostMapping("/customers/{id}/make-admin")
    public String makeAdmin(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        return updateUserRole(id, "admin", true, redirectAttributes);
    }
    
    /**
     * Remove admin status from a user
     */
    @PostMapping("/customers/{id}/remove-admin")
    public String removeAdmin(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if (!isAdminMode(session, redirectAttributes)) {
            return "redirect:/";
        }
        
        return updateUserRole(id, "admin", false, redirectAttributes);
    }
    
    /**
     * Helper method to check if user is in admin mode
     */
    private boolean isAdminMode(HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        Boolean adminMode = (Boolean) session.getAttribute(ADMIN_MODE_ATTR);
        
        if (loggedInUser == null || !loggedInUser.isAdmin() || adminMode == null || !adminMode.booleanValue()) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Access denied. Please switch to admin mode.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Helper method to update user roles
     */
    private String updateUserRole(Integer id, String roleType, boolean addRole, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Customer not found.");
            return "redirect:/admin/customers";
        }
        
        User customer = userOpt.get();
        String action = addRole ? "added" : "removed";
        String preposition = addRole ? "to" : "from";
        
        try {
            if (roleType.equals("employee")) {
                customer.setEmployee(addRole);
            } else if (roleType.equals("admin")) {
                customer.setAdmin(addRole);
            }
            
            userService.updateUser(customer);
            redirectAttributes.addFlashAttribute("success", 
                "Employee role " + action + " " + preposition + " " + customer.getFirstName() + " " + customer.getLastName() + ".");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating customer role: " + e.getMessage());
        }
        
        return "redirect:/admin/customers/" + id;
    }
}