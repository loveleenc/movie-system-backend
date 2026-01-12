package com.bookit.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private List<String> roles;

    public UserDto(@NonNull String firstName,
                   @NonNull String lastName,
                   @NonNull String username,
                   @NonNull String password,
                   @NonNull String email,
                   @NonNull List<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public UserDto(){}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }


}
