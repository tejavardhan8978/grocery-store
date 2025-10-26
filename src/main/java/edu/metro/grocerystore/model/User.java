package edu.metro.grocerystore.model;

import jakarta.persistence.*;


@Entity
// https://stackoverflow.com/a/75648759 - Don't use SQL/postgresql keywords then everything will work :)
// Keyword appendix https://www.postgresql.org/docs/current/sql-keywords-appendix.html
@Table(name="appusers")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userid",insertable=false, updatable=false)
    private Integer userid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "is_guest")
    private boolean isGuest;

    @Column(name = "is_employee")
    private boolean isEmployee;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToOne(mappedBy = "appusers")
    private Address address;


    @OneToOne(mappedBy = "appusers")
    private Cart cart = new Cart();

    public User() {}

    public User(Integer userid, String firstName, String lastName, String email, String password, String phone, boolean isGuest, boolean isEmployee, boolean isAdmin) {
        this.userid = userid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isGuest = isGuest;
        this.isEmployee = isEmployee;
        this.isAdmin = isAdmin;
    }

    public Integer getId() {
        return userid;
    }

    public void setId(int id) {
        this.userid = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public boolean isAdmin() {return isAdmin;}

    public void setAdmin(boolean admin) {isAdmin = admin;}

    public boolean isEmployee() {return isEmployee;}

    public void setEmployee(boolean employee) {isEmployee = employee;}

    public Cart getCart() {return cart;}

}
