package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Find user by email address
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by email and password for authentication
     * @param email the email address
     * @param password the password
     * @return Optional containing the user if credentials match
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    /**
     * Check if user exists by email
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find all admin users
     * @return list of admin users
     */
    @Query("SELECT u FROM User u WHERE u.isAdmin = true")
    java.util.List<User> findAllAdmins();
    
    /**
     * Find all employee users
     * @return list of employee users
     */
    @Query("SELECT u FROM User u WHERE u.isEmployee = true")
    java.util.List<User> findAllEmployees();
    
    /**
     * Search users by name, email, phone, or user ID
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of matching users
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "CAST(u.userid AS string) LIKE CONCAT('%', :searchTerm, '%')")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
}