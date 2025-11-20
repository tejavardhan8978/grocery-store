package edu.metro.grocerystore.service;

import edu.metro.grocerystore.model.Order;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Authenticate user with email and password
     * @param email user's email
     * @param password user's password
     * @return Optional containing authenticated user if credentials are valid
     */
    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    /**
     * Register a new user
     * @param user the user to register
     * @return the saved user
     * @throws IllegalArgumentException if email already exists
     */
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        // Set default values for new users
        user.setGuest(false);
        user.setEmployee(false);
        user.setAdmin(false);
        user.setOrders(new ArrayList<Order>(0));
        return userRepository.save(user);
    }
    
    /**
     * Find user by ID
     * @param id user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
    
    /**
     * Find user by email
     * @param email user's email
     * @return Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Create a guest user for non-authenticated sessions
     * @return a guest user instance
     */
    public User createGuestUser() {
        User guestUser = new User();
        guestUser.setGuest(true);
        guestUser.setFirstName("Guest");
        guestUser.setLastName("User");
        return guestUser;
    }
    
    /**
     * Update user information
     * @param user the user to update
     * @return the updated user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Get all users with pagination
     * @param pageable pagination information
     * @return page of users
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    /**
     * Search users by name, email, phone, or user ID
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of matching users
     */
    public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable);
    }
    
    /**
     * Delete a user by ID
     * @param id the user ID to delete
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}