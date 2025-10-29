package edu.metro.grocerystore.DTO;

public class UserLoginDTO {

    private String email;
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPasswordMatch(String password) {
        return this.password.equals(password);
    }
}
