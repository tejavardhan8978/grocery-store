package edu.metro.grocerystore.DTO;

import edu.metro.grocerystore.model.Address;
import edu.metro.grocerystore.model.Order;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {


    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
    private boolean isGuest;
    private boolean isEmployee;
    private boolean isAdmin;
    private List<Order> orders;


    private boolean isActive;

    public UserDTO() {
    }

    public UserDTO(int id, String firstName, String lastName, String email, String phone, Address address, ArrayList<Order> orders, boolean isGuest, boolean isEmployee, boolean isAdmin, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.orders = orders;
        this.isGuest = isGuest;
        this.isEmployee = isEmployee;
        this.isAdmin = isAdmin;
        this.isActive = isActive;

    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmployee() {return isEmployee;}

    public void setEmployee(boolean employee) {isEmployee = employee;}

    public boolean isAdmin() {return isAdmin;}

    public void setAdmin(boolean admin) {isAdmin = admin;}

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
