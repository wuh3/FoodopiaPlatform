package com.foodopia.app.dto;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationDto {

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Username must be at least 6 characters long")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only letters and numbers")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    // Must contain at least one lower case letter, one upper case letter, and one digit.
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain lower case, upper case letters and a digit")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @Nullable
    private String alias;

    @Nullable
    private String phone;

    @Nullable
    private String address;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;
    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String alias) {
        this.address = address;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getAlias() {
        return alias;
    }

    public void setAlias(@Nullable String alias) {
        this.alias = alias;
    }
}